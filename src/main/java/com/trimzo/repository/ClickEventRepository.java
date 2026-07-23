package com.trimzo.repository;

import com.trimzo.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

    List<ClickEvent> findByShortUrlId(Long shortUrlId);

    long countByShortUrlId(Long shortUrlId);
}