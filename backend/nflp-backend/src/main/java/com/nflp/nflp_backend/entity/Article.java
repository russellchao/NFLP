package com.nflp.nflp_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(unique = true, nullable = false, length = 500)
    private String url;

    @Column(nullable = false, length = 100)
    private String source;  // ESPN, NFL.com, etc.

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "fetched_at")
    private LocalDateTime fetchedAt;

    @Column(nullable = false)
    private Boolean analyzed = false;  // Default to false

    // Store as comma-separated strings (or use JSON/array in production)
    @Column(name = "mentioned_players", columnDefinition = "TEXT")
    private String mentionedPlayers;

    @Column(name = "mentioned_teams", columnDefinition = "TEXT")
    private String mentionedTeams;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Automatically set timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (fetchedAt == null) {
            fetchedAt = LocalDateTime.now();
        }
        if (analyzed == null) {
            analyzed = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}