package com.trimzo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ShortenRequest(

    @NotBlank(message = "URL is required")
    @Pattern(
        regexp = "^https?://.+",
        message = "URL must start with http:// or https://"
    )
    String originalUrl,

    @Size(min = 3, max = 10, message = "Custom alias must be 3-10 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9_-]*$",
        message = "Custom alias can only contain letters, numbers, hyphens, underscores"
    )
    String customAlias

) {}