package com.trimzo.controller;

import com.trimzo.dto.ShortUrlResponse;
import com.trimzo.dto.ShortenRequest;
import com.trimzo.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<ShortUrlResponse> shorten(
            @Valid @RequestBody ShortenRequest request,
            Authentication authentication) {

        String userEmail = (authentication != null) ? authentication.getName() : null;

        ShortUrlResponse response = urlService.shorten(request, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}