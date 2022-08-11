package com.awilkinson.TPXDemo.repository;

import com.awilkinson.TPXDemo.domain.ShortenedURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortenedURLRepository extends JpaRepository<ShortenedURL, Long> {
}
