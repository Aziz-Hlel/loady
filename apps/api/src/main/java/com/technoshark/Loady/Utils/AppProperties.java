package com.technoshark.Loady.Utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "app") // to help retreive values from application.yml under app attribute
@Validated
public record AppProperties(
                String userId,
                @NotBlank @NotNull String firebaseServiceAccount) {

}
