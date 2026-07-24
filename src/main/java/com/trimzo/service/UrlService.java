package com.trimzo.service;

import com.trimzo.dto.ShortenRequest;
import com.trimzo.dto.ShortUrlResponse;
import com.trimzo.entity.ShortUrl;
import com.trimzo.entity.User;
import com.trimzo.repository.ShortUrlRepository;
import com.trimzo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class UrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;
    private final ShortCodeService shortCodeService;

    @Value("${trimzo.base-url}")
    private String baseUrl;

    public UrlService(ShortUrlRepository shortUrlRepository,
                      UserRepository userRepository,
                      ShortCodeService shortCodeService) {
        this.shortUrlRepository = shortUrlRepository;
        this.userRepository = userRepository;
        this.shortCodeService = shortCodeService;
    }

    public ShortUrlResponse shorten(ShortenRequest request, String userEmail) {

        String code;
        boolean isCustom = false;

        // 1. Decide the short code: custom alias or generated
        if (request.customAlias() != null && !request.customAlias().isBlank()) {
            if (shortUrlRepository.existsByShortCode(request.customAlias())) {
                throw new IllegalArgumentException(
                        "Alias '" + request.customAlias() + "' is already taken");
            }
            code = request.customAlias();
            isCustom = true;
        } else {
            code = shortCodeService.generateCode();
        }

        // 2. Attach an owner ONLY if someone is logged in (hybrid model)
        User owner = null;
        if (userEmail != null) {
            owner = userRepository.findByEmail(userEmail).orElse(null);
        }

        // 3. Build and save
        ShortUrl shortUrl = ShortUrl.builder()
                .shortCode(code)
                .originalUrl(request.originalUrl())
                .user(owner)
                .customAlias(isCustom)
                .build();

        ShortUrl saved = shortUrlRepository.save(shortUrl);

        return toResponse(saved);
    }

    public String resolve(String shortCode) {

        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No link found for code: " + shortCode));

        if (shortUrl.getExpiresAt() != null
                && shortUrl.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("This link has expired");
        }

        return shortUrl.getOriginalUrl();
    }

    public List<ShortUrlResponse> getMyUrls(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return shortUrlRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteUrl(Long id, String userEmail) {

        ShortUrl shortUrl = shortUrlRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Link not found"));

        if (shortUrl.getUser() == null
                || !shortUrl.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("You don't own this link");
        }

        shortUrlRepository.delete(shortUrl);
    }

    private ShortUrlResponse toResponse(ShortUrl entity) {
        return new ShortUrlResponse(
                entity.getId(),
                entity.getShortCode(),
                baseUrl + "/" + entity.getShortCode(),
                entity.getOriginalUrl(),
                entity.getExpiresAt(),
                entity.getCreatedAt()
        );
    }
}