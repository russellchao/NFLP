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

    // Find by player (unique constraint)
    Optional<PlayerInsight> findByPlayerIgnoreCase(String player);

    // Get the total number of player insights
    long count();

//    // Find by player ID, ordered by date
//    List<PlayerInsight> findByPlayerIdOrderByDateDesc(Long playerId);
//
//    // Find recent insights for a player (last N days)
//    @Query("SELECT pi FROM PlayerInsight pi WHERE pi.player.id = :playerId AND pi.date >= :startDate ORDER BY pi.date DESC")
//    List<PlayerInsight> findRecentInsights(
//            @Param("playerId") Long playerId,
//            @Param("startDate") LocalDate startDate
//    );
//
//    // Find top players by sentiment on a specific date
//    @Query("SELECT pi FROM PlayerInsight pi WHERE pi.date = :date ORDER BY pi.avgSentiment DESC")
//    List<PlayerInsight> findTopPlayersByDate(@Param("date") LocalDate date);
//
//    // Find trending players (high mention count)
//    @Query("SELECT pi FROM PlayerInsight pi WHERE pi.date = :date ORDER BY pi.mentionCount DESC")
//    List<PlayerInsight> findTrendingPlayersByDate(@Param("date") LocalDate date);
//
//    // Get average sentiment for a player over time
//    @Query("SELECT AVG(pi.avgSentiment) FROM PlayerInsight pi WHERE pi.player.id = :playerId")
//    BigDecimal getAverageSentimentForPlayer(@Param("playerId") Long playerId);
//
//    // Get total mentions for a player
//    @Query("SELECT SUM(pi.mentionCount) FROM PlayerInsight pi WHERE pi.player.id = :playerId")
//    Long getTotalMentionsForPlayer(@Param("player") Long playerId);
//
//    // Find insights with sentiment above threshold
//    @Query("SELECT pi FROM PlayerInsight pi WHERE pi.avgSentiment > :threshold ORDER BY pi.avgSentiment DESC")
//    List<PlayerInsight> findPositiveInsights(@Param("threshold") BigDecimal threshold);
//
//    // Check if insight exists for player and date
//    boolean existsByPlayerIdAndDate(Long playerId, LocalDate date);
//
//    // Delete old insights (older than a certain date)
//    void deleteByDateBefore(LocalDate date);
}
