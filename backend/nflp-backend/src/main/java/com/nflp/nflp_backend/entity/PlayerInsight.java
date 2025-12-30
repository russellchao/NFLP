package com.nflp.nflp_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "player_insights",
        uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "date"}))
public class PlayerInsight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "player_id", nullable = false)
//    private Player player;
//
//    @Column(nullable = false)
//    private LocalDate date;

    @Column(name = "player")
    private String player;

    @Column(name = "avg_sentiment", precision = 3, scale = 2)
    private BigDecimal avgSentiment;

    @Column(name = "mention_count")
    private Integer mentionCount = 0;

    @Column(name = "top_keywords", columnDefinition = "TEXT")
    private String topKeywords;  // Comma-separated keywords

//    @Column(name = "insight_summary", columnDefinition = "TEXT")
//    private String insightSummary;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (mentionCount == null) {
            mentionCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
