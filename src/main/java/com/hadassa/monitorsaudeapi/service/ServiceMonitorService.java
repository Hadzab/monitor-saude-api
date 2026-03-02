package com.hadassa.monitorsaudeapi.service;

import com.hadassa.monitorsaudeapi.entity.ServiceMonitor;
import com.hadassa.monitorsaudeapi.repository.ServiceMonitorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceMonitorService {

    private final ServiceMonitorRepository repository;

    public ServiceMonitorService(ServiceMonitorRepository repository) {
        this.repository = repository;
    }

    // ============================
    // CRUD — CREATE
    // ============================
    public ServiceMonitor create(ServiceMonitor serviceMonitor) {
        return repository.save(serviceMonitor);
    }

    // ============================
    // CRUD — READ (todos)
    // ============================
    public List<ServiceMonitor> findAll() {
        return repository.findAll();
    }

    // ============================
    // CRUD — DELETE
    // ============================
    public void delete(Long id) {
        ServiceMonitor service = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado: " + id));
        repository.delete(service);
    }

    // ============================
    // CRUD — UPDATE
    // ============================
    public ServiceMonitor update(Long id, ServiceMonitor serviceMonitor) {
        ServiceMonitor existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado: " + id));

        existing.setName(serviceMonitor.getName());
        existing.setUrl(serviceMonitor.getUrl());
        existing.setEnabled(serviceMonitor.isEnabled());
        existing.setCheckInterval(serviceMonitor.getCheckInterval());

        return repository.save(existing);
    }

    // ============================
    // EXEMPLOS INICIAIS (POSTCONSTRUCT)
    // ============================
    @PostConstruct
    public void initExamples() {

        boolean downExists = repository.existsByName("Teste DOWN");
        boolean upExists   = repository.existsByName("Teste UP");

        if (!downExists) {
            ServiceMonitor downExample = ServiceMonitor.builder()
                    .name("Teste DOWN")
                    .url("http://localhost:9999/fake")
                    .enabled(true)
                    .checkInterval(60)
                    .build();
            repository.save(downExample);
        }

        if (!upExists) {
            ServiceMonitor upExample = ServiceMonitor.builder()
                    .name("Teste UP")
                    .url("https://jsonplaceholder.typicode.com/todos/1")
                    .enabled(true)
                    .checkInterval(60)
                    .build();
            repository.save(upExample);
        }

        System.out.println("Serviços de teste verificados e inseridos se necessário!");
    }
}