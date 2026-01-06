package com.nflp.nflp_backend.repository;

import com.nflp.nflp_backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Find unanalyzed articles
    List<Article> findByAnalyzedFalse();

    // Find analyzed articles
    List<Article> findByAnalyzedTrue();

    // Find by title
    List<Article> findByTitleContainingIgnoreCase(String keyword);

    // Find by source
    List<Article> findBySource(String source);

    // Find by URL (for checking duplicates)
    Optional<Article> findByUrl(String url);

    // Find recent articles
    List<Article> findTop10ByOrderByPublishedAtDesc();

    // Find articles published after a certain date
    List<Article> findByPublishedAtAfter(LocalDateTime date);

    // Find articles by source and analyzed status
    List<Article> findBySourceAndAnalyzedFalse(String source);

    // Find articles mentioning a player
    List<Article> findByMentionedPlayersContaining(String playerName);

    // Find articles mentioning a team
    List<Article> findByMentionedTeamsContaining(String teamName);

    // Count unanalyzed articles
    long countByAnalyzedFalse();

    // Count analyzed articles
    long countByAnalyzedTrue();

    // Count by source
    long countBySource(String source);
}