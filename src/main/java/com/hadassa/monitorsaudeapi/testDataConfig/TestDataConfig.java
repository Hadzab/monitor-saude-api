package com.hadassa.monitorsaudeapi.testDataConfig;

import com.hadassa.monitorsaudeapi.entity.ServiceMonitor;
import com.hadassa.monitorsaudeapi.repository.ServiceMonitorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDataConfig {

    @Bean
    public CommandLineRunner insertTestServices(ServiceMonitorRepository monitorRepository) {
        return args -> {

            // Insere serviço de teste DOWN caso não exista
            if (monitorRepository.findByUrl("http://localhost:9999/fake").isEmpty()) {
                ServiceMonitor downService = ServiceMonitor.builder()
                        .name("Teste DOWN")
                        .url("http://localhost:9999/fake")
                        .enabled(true)
                        .checkInterval(60)
                        .build();
                monitorRepository.save(downService);
            }

            // Insere serviço de teste UP caso não exista
            if (monitorRepository.findByUrl("https://jsonplaceholder.typicode.com/todos/1").isEmpty()) {
                ServiceMonitor upService = ServiceMonitor.builder()
                        .name("Teste UP")
                        .url("https://jsonplaceholder.typicode.com/todos/1")
                        .enabled(true)
                        .checkInterval(60)
                        .build();
                monitorRepository.save(upService);
            }

            System.out.println("Serviços de teste inseridos no banco!");
        };
    }
}