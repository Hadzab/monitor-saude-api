package com.hadassa.monitorsaudeapi.controller;

import com.hadassa.monitorsaudeapi.entity.ServiceStatusHistory;
import com.hadassa.monitorsaudeapi.repository.ServiceStatusHistoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ServiceHistoryController {

    private final ServiceStatusHistoryRepository historyRepository;

    public ServiceHistoryController(ServiceStatusHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @GetMapping("/history")
    public List<ServiceStatusHistory> getHistory(
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) Integer limit
    ) {
        // Busca todos os registros
        List<ServiceStatusHistory> allHistory = historyRepository.findAll();

        // Filtra por serviço, se informado
        if (serviceId != null) {
            allHistory = allHistory.stream()
                    .filter(h -> h.getService().getId().equals(serviceId))
                    .collect(Collectors.toList());
        }

        // Aplica limite de registros, se informado
        if (limit != null && limit > 0 && allHistory.size() > limit) {
            allHistory = allHistory.subList(0, limit); // limita quantidade de registros retornados
        }

        return allHistory;
    }
}