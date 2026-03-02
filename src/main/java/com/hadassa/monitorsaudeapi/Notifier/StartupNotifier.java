package com.hadassa.monitorsaudeapi.Notifier;

import com.hadassa.monitorsaudeapi.service.MonitoringService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupNotifier {

    private final MonitoringService monitoringService;

    public StartupNotifier(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendStartupMessage() {
        System.out.println("Aplicação iniciada. Enviando alerta para o Discord...");
        monitoringService.testDiscordAlert();
    }
}
