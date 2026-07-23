package com.trimzo.repository;

import com.trimzo.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    List<ShortUrl> findByUserId(Long userId);

    @Query(value = "SELECT nextval('short_code_seq')", nativeQuery = true)
    long nextSequenceValue();
}