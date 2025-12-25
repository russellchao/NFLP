package com.nflp.nflp_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fetch_jobs")
public class FetchJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_type", nullable = false, length = 50)
    private String jobType;  // "NEWS_FETCH", "REDDIT_FETCH", "TWITTER_FETCH"

    @Column(nullable = false, length = 20)
    private String status;  // "IN_PROGRESS", "COMPLETED", "FAILED"

    @Column(name = "items_fetched")
    private Integer itemsFetched = 0;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "IN_PROGRESS";
        }
        if (itemsFetched == null) {
            itemsFetched = 0;
        }
    }
}
