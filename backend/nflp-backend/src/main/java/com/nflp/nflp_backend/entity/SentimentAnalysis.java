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
@Table(name = "sentiment_analysis",
        uniqueConstraints = @UniqueConstraint(columnNames = {"content_type", "content_id"}))
public class SentimentAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type", nullable = false, length = 20)
    private String contentType;  // "ARTICLE" or "SOCIAL_POST"

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "sentiment_score", precision = 3, scale = 2)
    private BigDecimal sentimentScore;  // -1.00 to 1.00

    @Column(name = "sentiment_label", length = 20)
    private String sentimentLabel;  // POSITIVE, NEGATIVE, NEUTRAL

    @Column(name = "toxicity_score", precision = 3, scale = 2)
    private BigDecimal toxicityScore;  // 0.00 to 1.00

    @Column(name = "extracted_entities", columnDefinition = "TEXT")
    private String extractedEntities;  // JSON string of players/teams

    // VADER sentiment breakdown
    @Column(precision = 3, scale = 2)
    private BigDecimal positive;

    @Column(precision = 3, scale = 2)
    private BigDecimal negative;

    @Column(precision = 3, scale = 2)
    private BigDecimal neutral;

    @Column(precision = 3, scale = 2)
    private BigDecimal compound;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;

    @PrePersist
    protected void onCreate() {
        analyzedAt = LocalDateTime.now();
    }
}
