package com.trimzo.dto;

public record AuthResponse(
    String token,
    String email
) {}