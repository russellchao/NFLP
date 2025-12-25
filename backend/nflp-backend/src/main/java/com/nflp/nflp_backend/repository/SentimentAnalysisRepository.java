package com.nflp.nflp_backend.repository;

import com.nflp.nflp_backend.entity.SentimentAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SentimentAnalysisRepository extends JpaRepository<SentimentAnalysis, Long> {

    // Find by content type and content ID (for checking if already analyzed)
    Optional<SentimentAnalysis> findByContentTypeAndContentId(String contentType, Long contentId);

    // Find by content type
    List<SentimentAnalysis> findByContentType(String contentType);

    // Find analyzed after a certain date
    List<SentimentAnalysis> findByAnalyzedAtAfter(LocalDateTime date);

    // Find by sentiment label
    List<SentimentAnalysis> findBySentimentLabel(String label);

    // Find toxic content (toxicity score > threshold)
    @Query("SELECT s FROM SentimentAnalysis s WHERE s.toxicityScore > :threshold")
    List<SentimentAnalysis> findToxicContent(@Param("threshold") BigDecimal threshold);

    // Find by content type and toxic
    @Query("SELECT s FROM SentimentAnalysis s WHERE s.contentType = :contentType AND s.toxicityScore > :threshold")
    List<SentimentAnalysis> findToxicContentByType(
            @Param("contentType") String contentType,
            @Param("threshold") BigDecimal threshold
    );

    // Get average sentiment by content type
    @Query("SELECT AVG(s.sentimentScore) FROM SentimentAnalysis s WHERE s.contentType = :contentType")
    BigDecimal getAverageSentimentByType(@Param("contentType") String contentType);

    // Get average toxicity
    @Query("SELECT AVG(s.toxicityScore) FROM SentimentAnalysis s")
    BigDecimal getAverageToxicity();

    // Count by sentiment label
    long countBySentimentLabel(String label);

    // Count toxic posts
    @Query("SELECT COUNT(s) FROM SentimentAnalysis s WHERE s.toxicityScore > :threshold")
    long countToxicContent(@Param("threshold") BigDecimal threshold);

    // Delete by content type and content ID
    void deleteByContentTypeAndContentId(String contentType, Long contentId);

    // Check if content is already analyzed
    boolean existsByContentTypeAndContentId(String contentType, Long contentId);
}