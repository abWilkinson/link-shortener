package com.awilkinson.TPXDemo.domain;

import javax.persistence.*;

/**
 * A database entity record representing a shortened URL.
 */
@Entity
@Table
public class ShortenedURL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fullURL;

    public ShortenedURL() {

    }
    public ShortenedURL(String fullURL) {
        this.fullURL = fullURL;
    }
    public ShortenedURL(long id, String fullURL) {
        this.id = id;
        this.fullURL = fullURL;
    }

    public long getId() {
        return id;
    }

    public String getFullURL() {
        return fullURL;
    }
}
