package com.nflp.nflp_backend.repository;

import com.nflp.nflp_backend.entity.SocialPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SocialPostRepository extends JpaRepository<SocialPost, Long> {

    // Find unanalyzed posts
    List<SocialPost> findByAnalyzedFalse();

    // Find analyzed posts
    List<SocialPost> findByAnalyzedTrue();

    // Find by platform
    List<SocialPost> findByPlatform(String platform);

    // Find by platform and unanalyzed
    List<SocialPost> findByPlatformAndAnalyzedFalse(String platform);

    // Find by postId (for checking duplicates)
    Optional<SocialPost> findByPostId(String postId);

    // Find by subreddit
    List<SocialPost> findBySubreddit(String subreddit);

    // Find recent posts
    List<SocialPost> findTop10ByOrderByPostedAtDesc();

    // Find posts between dates
    List<SocialPost> findByPostedAtBetween(LocalDateTime start, LocalDateTime end);

    // Find posts mentioning a player
    List<SocialPost> findByMentionedPlayersContaining(String playerName);

    // Find posts mentioning a team
    List<SocialPost> findByMentionedTeamsContaining(String teamName);

    // Find posts by subreddit and unanalyzed
    List<SocialPost> findBySubredditAndAnalyzedFalse(String subreddit);

    // Count unanalyzed posts
    long countByAnalyzedFalse();

    // Count by platform
    long countByPlatform(String platform);
}