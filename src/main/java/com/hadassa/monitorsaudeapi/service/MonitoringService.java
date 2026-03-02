package com.hadassa.monitorsaudeapi.service;

import com.hadassa.monitorsaudeapi.entity.ServiceMonitor;
import com.hadassa.monitorsaudeapi.entity.ServiceStatusHistory;
import com.hadassa.monitorsaudeapi.repository.ServiceMonitorRepository;
import com.hadassa.monitorsaudeapi.repository.ServiceStatusHistoryRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MonitoringService {

    private final ServiceMonitorRepository monitorRepository;
    private final ServiceStatusHistoryRepository historyRepository;
    private final WebClient webClient;
    private final RestTemplate restTemplate;

    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;

    public MonitoringService(ServiceMonitorRepository monitorRepository,
                             ServiceStatusHistoryRepository historyRepository) {
        this.monitorRepository = monitorRepository;
        this.historyRepository = historyRepository;
        this.webClient = WebClient.create();
        this.restTemplate = new RestTemplate();
    }

    // ============================
    // MONITORAMENTO AUTOMÁTICO
    // ============================
    @Scheduled(fixedDelayString = "${monitor.delay}")
    public void autoCheck() {
        System.out.println("\n==============================");
        System.out.println("AUTO CHECK executado em: " + LocalDateTime.now());
        checkAllServices();
        System.out.println("==============================\n");
    }

    public void checkAllServices() {

        long now = System.currentTimeMillis();
        List<ServiceMonitor> allServices = monitorRepository.findAll();
        boolean hasEnabledService = false;

        for (ServiceMonitor service : allServices) {

            if (!service.isEnabled()) {
                continue;
            }

            hasEnabledService = true;
            long lastChecked = service.getLastChecked() != null ? service.getLastChecked() : 0L;

            if (now - lastChecked >= service.getCheckInterval() * 1000) {

                System.out.println(
                        "Verificando serviço: " + service.getName()
                                + " | Horário: " + LocalDateTime.now()
                );

                checkService(service);

                service.setLastChecked(now);
                monitorRepository.save(service);
            }
        }

        if (!hasEnabledService) {
            System.out.println("⚠ Nenhum serviço cadastrado ou habilitado.");
        }
    }

    // ============================
    // CHECAGEM OTIMIZADA
    // ============================
    public void checkService(ServiceMonitor service) {

        String status;
        int httpCode = 0;
        long responseTime = 0;
        long start = System.currentTimeMillis();

        try {
            webClient.get()
                    .uri(service.getUrl())
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            responseTime = System.currentTimeMillis() - start;
            status = "UP";
            httpCode = 200;

        } catch (WebClientResponseException e) {
            status = "DOWN";
            httpCode = e.getStatusCode().value();

        } catch (Exception e) {
            status = "DOWN";
        }

        boolean isUp = "UP".equals(status);

        ServiceStatusHistory lastHistory = historyRepository
                .findFirstByServiceOrderByTimestampDesc(service)
                .orElse(null);

        boolean statusChanged = lastHistory == null || lastHistory.getUp() != isUp;

        ServiceStatusHistory newHistory = ServiceStatusHistory.builder()
                .service(service)
                .up(isUp)
                .httpStatus(httpCode)
                .responseTime(responseTime)
                .timestamp(LocalDateTime.now())
                .build();

        historyRepository.save(newHistory);

        if (statusChanged) {
            sendDiscordAlert(service.getName(), status, httpCode, responseTime);
        }

        if (service.isUp() != isUp) {
            service.setUp(isUp);
            monitorRepository.save(service);
        }
    }

    // ============================
    // ALERTA AUTOMÁTICO
    // ============================
    public void sendDiscordAlert(String serviceName, String status, int httpCode, long responseTime) {

        String emoji = status.equals("UP") ? "✅" : "❌";

        String messageJson = "{ \"content\": \"" + emoji +
                " Serviço **" + serviceName + "** está " + status +
                " (HTTP: " + httpCode + ", Tempo: " + responseTime + "ms)\" }";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(messageJson, headers);

            restTemplate.postForEntity(discordWebhookUrl, entity, String.class);

            System.out.println("Alerta enviado: " + serviceName + " -> " + status);

        } catch (Exception e) {
            System.err.println("Erro ao enviar alerta para Discord: " + e.getMessage());
        }
    }

    // ============================
    // ALERTA MANUAL
    // ============================
    public void sendManualUpdateAlert(String serviceName, boolean active) {

        String status = active ? "ATIVADO 🟢" : "DESATIVADO 🔴";

        String messageJson = "{ \"content\": \"⚙️ Serviço atualizado manualmente!\\n" +
                "Nome: " + serviceName + "\\n" +
                "Novo status: " + status + "\" }";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(messageJson, headers);

            restTemplate.postForEntity(discordWebhookUrl, entity, String.class);

            System.out.println("Alerta manual enviado: " + serviceName);

        } catch (Exception e) {
            System.err.println("Erro ao enviar alerta manual: " + e.getMessage());
        }
    }

    // ============================
    // ALERTA DE TESTE
    // ============================
    public void testDiscordAlert() {

        String messageJson = "{ \"content\": \"🚨 MonitoringService iniciado com sucesso!\" }";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(messageJson, headers);

            restTemplate.postForEntity(discordWebhookUrl, entity, String.class);

            System.out.println("Mensagem de teste enviada com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem de teste: " + e.getMessage());
        }
    }

    // ============================
    // LISTAR HISTÓRICO POR SERVIÇO
    // ============================
    public List<ServiceStatusHistory> getServiceHistory(Long serviceId) {

        ServiceMonitor service = monitorRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        return historyRepository.findByServiceOrderByTimestampDesc(service);
    }
}