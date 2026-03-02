package com.hadassa.monitorsaudeapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceMonitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;

    @Column(nullable = false)
    private boolean enabled = true; // Controle manual

    @Column(nullable = false)
    private boolean up = false;     // Status real monitorado

    private Integer checkInterval;

    private Long lastChecked;        // Timestamp do último check

    @OneToMany(
            mappedBy = "service",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<ServiceStatusHistory> statusHistory;
}