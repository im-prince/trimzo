package com.trimzo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
    name = "click_events",
    indexes = {
        @Index(name = "idx_short_url_id", columnList = "short_url_id"),
        @Index(name = "idx_clicked_at", columnList = "clicked_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "short_url_id")
    private ShortUrl shortUrl;

    @Column(name = "clicked_at", nullable = false, updatable = false)
    private Instant clickedAt;

    @Column(length = 500)
    private String referrer;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", length = 20)
    private DeviceType deviceType;

    @Column(length = 50)
    private String browser;

    @Column(length = 100)
    private String country;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @PrePersist
    void onCreate() {
        if (clickedAt == null) clickedAt = Instant.now();
    }
}