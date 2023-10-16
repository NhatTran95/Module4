package com.example.book_manager.uploader;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "application.uploader")
public class CloudinaryConfig {
    @Value("${application.uploader.cloud-name}")
    private String cloudName;
    @Value("${application.uploader.api-key}")
    private String apiKey;
    @Value("${application.uploader.api-secret}")
    private String apiSecret;
}
