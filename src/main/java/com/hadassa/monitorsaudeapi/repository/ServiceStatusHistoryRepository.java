package com.hadassa.monitorsaudeapi.repository;

import com.hadassa.monitorsaudeapi.entity.ServiceMonitor;
import com.hadassa.monitorsaudeapi.entity.ServiceStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceStatusHistoryRepository
        extends JpaRepository<ServiceStatusHistory, Long> {

    // Busca todo histórico de um serviço, ordenado do mais recente ao antigo
    List<ServiceStatusHistory> findByServiceOrderByTimestampDesc(ServiceMonitor service);

    // Retorna apenas o último registro de histórico para um serviço específico
    Optional<ServiceStatusHistory> findFirstByServiceOrderByTimestampDesc(ServiceMonitor service);
}