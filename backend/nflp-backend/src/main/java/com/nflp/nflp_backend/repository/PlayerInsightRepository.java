package com.nflp.nflp_backend.repository;

import com.nflp.nflp_backend.entity.Player;
import com.nflp.nflp_backend.entity.PlayerInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerInsightRepository extends JpaRepository<PlayerInsight, Long> {

    // Find by player and date (unique constraint)
    Optional<PlayerInsight> findByPlayerAndDate(Player player, LocalDate date);

    // Find by player ID and date
    Optional<PlayerInsight> findByPlayerIdAndDate(Long playerId, LocalDate date);

    // Find all insights for a player, ordered by date descending
    List<PlayerInsight> findByPlayerOrderByDateDesc(Player player);

    // Find by player ID, ordered by date
    List<PlayerInsight> findByPlayerIdOrderByDateDesc(Long playerId);

    // Find recent insights for a player (last N days)
    @Query("SELECT pi FROM PlayerInsight pi WHERE pi.player.id = :playerId AND pi.date >= :startDate ORDER BY pi.date DESC")
    List<PlayerInsight> findRecentInsights(
            @Param("playerId") Long playerId,
            @Param("startDate") LocalDate startDate
    );

    // Find insights for a date range
    List<PlayerInsight> findByPlayerIdAndDateBetween(Long playerId, LocalDate startDate, LocalDate endDate);

    // Find insights by date
    List<PlayerInsight> findByDate(LocalDate date);

    // Find top players by sentiment on a specific date
    @Query("SELECT pi FROM PlayerInsight pi WHERE pi.date = :date ORDER BY pi.avgSentiment DESC")
    List<PlayerInsight> findTopPlayersByDate(@Param("date") LocalDate date);

    // Find trending players (high mention count)
    @Query("SELECT pi FROM PlayerInsight pi WHERE pi.date = :date ORDER BY pi.mentionCount DESC")
    List<PlayerInsight> findTrendingPlayersByDate(@Param("date") LocalDate date);

    // Get average sentiment for a player over time
    @Query("SELECT AVG(pi.avgSentiment) FROM PlayerInsight pi WHERE pi.player.id = :playerId")
    BigDecimal getAverageSentimentForPlayer(@Param("playerId") Long playerId);

    // Get total mentions for a player
    @Query("SELECT SUM(pi.mentionCount) FROM PlayerInsight pi WHERE pi.player.id = :playerId")
    Long getTotalMentionsForPlayer(@Param("playerId") Long playerId);

    // Find insights with sentiment above threshold
    @Query("SELECT pi FROM PlayerInsight pi WHERE pi.avgSentiment > :threshold ORDER BY pi.avgSentiment DESC")
    List<PlayerInsight> findPositiveInsights(@Param("threshold") BigDecimal threshold);

    // Check if insight exists for player and date
    boolean existsByPlayerIdAndDate(Long playerId, LocalDate date);

    // Delete old insights (older than a certain date)
    void deleteByDateBefore(LocalDate date);
}
