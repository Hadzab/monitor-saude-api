package com.hadassa.monitorsaudeapi.repository;

import com.hadassa.monitorsaudeapi.entity.ServiceMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceMonitorRepository extends JpaRepository<ServiceMonitor, Long> {
    Optional<ServiceMonitor> findByUrl(String url);
    boolean existsByName(String testeDown);
}