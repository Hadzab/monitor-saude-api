package com.hadassa.monitorsaudeapi.controller;

import com.hadassa.monitorsaudeapi.dto.ServiceMonitorRequestDTO;
import com.hadassa.monitorsaudeapi.dto.ServiceMonitorResponseDTO;
import com.hadassa.monitorsaudeapi.dto.UpdateServiceMonitorDTO;
import com.hadassa.monitorsaudeapi.entity.ServiceMonitor;
import com.hadassa.monitorsaudeapi.entity.ServiceStatusHistory;
import com.hadassa.monitorsaudeapi.repository.ServiceMonitorRepository;
import com.hadassa.monitorsaudeapi.service.MonitoringService;
import com.hadassa.monitorsaudeapi.service.ServiceMonitorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceMonitorController {

    private final ServiceMonitorService serviceMonitorService;
    private final MonitoringService monitoringService;
    private final ServiceMonitorRepository serviceRepository;

    public ServiceMonitorController(ServiceMonitorService serviceMonitorService,
                                    MonitoringService monitoringService,
                                    ServiceMonitorRepository serviceRepository) {
        this.serviceMonitorService = serviceMonitorService;
        this.monitoringService = monitoringService;
        this.serviceRepository = serviceRepository;
    }

    // =========================
    // LISTAR TODOS OS SERVIÇOS
    // =========================
    @GetMapping
    public List<ServiceMonitorResponseDTO> getAll() {
        return serviceMonitorService.findAll()
                .stream()
                .map(ServiceMonitorResponseDTO::new)
                .toList();
    }

    // =========================
    // HISTÓRICO DE UM SERVIÇO
    // =========================
    @GetMapping("/{id}/history")
    public ResponseEntity<List<ServiceStatusHistory>> getServiceHistory(@PathVariable Long id) {
        List<ServiceStatusHistory> history = monitoringService.getServiceHistory(id);
        return ResponseEntity.ok(history);
    }

    // =========================
    // CRIAR NOVO SERVIÇO
    // =========================
    @PostMapping
    public ResponseEntity<ServiceMonitorResponseDTO> createFixed(
            @RequestBody ServiceMonitorRequestDTO dto) {

        ServiceMonitor service = ServiceMonitor.builder()
                .name(dto.name())
                .url(dto.url())
                .enabled(true)
                .up(false)
                .checkInterval(60)
                .build();

        ServiceMonitor saved = serviceRepository.save(service);

        return ResponseEntity.ok(new ServiceMonitorResponseDTO(saved));
    }

    // =========================
    // ATUALIZAR SERVIÇO
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<ServiceMonitorResponseDTO> update(
            @PathVariable Long id,
            @RequestBody UpdateServiceMonitorDTO dto) {

        ServiceMonitor service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        Boolean oldEnabled = service.isEnabled();

        if (dto.name() != null) {
            service.setName(dto.name());
        }

        if (dto.url() != null) {
            service.setUrl(dto.url());
        }

        if (dto.enabled() != null) {
            service.setEnabled(dto.enabled());
        }

        ServiceMonitor updated = serviceRepository.save(service);

        // Dispara alerta se houve alteração manual no status enabled
        if (dto.enabled() != null && !oldEnabled.equals(dto.enabled())) {
            monitoringService.sendManualUpdateAlert(
                    updated.getName(),
                    updated.isEnabled()
            );
        }

        return ResponseEntity.ok(new ServiceMonitorResponseDTO(updated));
    }

    // =========================
    // DELETAR SERVIÇO
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceMonitorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}