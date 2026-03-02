package com.hadassa.monitorsaudeapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public record UpdateServiceMonitorDTO (
    String name,
    String url,
    Boolean enabled
    ) {}
