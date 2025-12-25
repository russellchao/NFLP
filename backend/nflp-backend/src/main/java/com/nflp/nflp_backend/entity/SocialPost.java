package com.nflp.nflp_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "social_posts")
public class SocialPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String platform;  // REDDIT or TWITTER

    @Column(name = "post_id", nullable = false, unique = true, length = 100)
    private String postId;  // Original platform post ID

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 100)
    private String author;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @Column(name = "fetched_at")
    private LocalDateTime fetchedAt;

    @Column(length = 100)
    private String subreddit;  // For Reddit posts (e.g., "r/nfl")

    @Column
    private Integer upvotes = 0;

    @Column
    private Integer comments = 0;

    @Column(nullable = false)
    private Boolean analyzed = false;

    @Column(name = "mentioned_players", columnDefinition = "TEXT")
    private String mentionedPlayers;

    @Column(name = "mentioned_teams", columnDefinition = "TEXT")
    private String mentionedTeams;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
        if (upvotes == null) {
            upvotes = 0;
        }
        if (comments == null) {
            comments = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
