package com.hadassa.monitorsaudeapi.dto;

import com.hadassa.monitorsaudeapi.entity.ServiceMonitor;
import lombok.Getter;

@Getter
public class ServiceMonitorResponseDTO {

    private final Long id;
    private final String name;
    private final String url;
    private final boolean enabled;
    private final Integer checkInterval;

    public ServiceMonitorResponseDTO(ServiceMonitor service) {
        this.id = service.getId();
        this.name = service.getName();
        this.url = service.getUrl();
        this.enabled = service.isEnabled();
        this.checkInterval = service.getCheckInterval();
    }
}
