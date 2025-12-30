package com.nflp.nflp_backend.service;

import com.nflp.nflp_backend.entity.Article;
import com.nflp.nflp_backend.entity.PlayerInsight;
import com.nflp.nflp_backend.entity.SentimentAnalysis;
import com.nflp.nflp_backend.entity.SocialPost;
import com.nflp.nflp_backend.entity.Player;
import com.nflp.nflp_backend.repository.*;
import com.nflp.nflp_backend.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerInsightService {

    private final SentimentAnalysisRepository sentimentAnalysisRepository;
    private final ArticleRepository articleRepository;
    private final SocialPostRepository socialPostRepository;
    private final PlayerRepository playerRepository;
    private final PlayerInsightRepository playerInsightRepository;


    /**
     * Find all articles mentioning a specific player
     *
     * @param playerName
     * @return List of articles with the player mentioned.
     */
    public List<Article> findArticlesWithPlayer(String playerName) {
        try {
            return articleRepository.findByMentionedPlayersContaining(playerName);
        } catch (Exception e) {
            log.error("Error finding articles with player {}: {}", playerName, e.getMessage());
            return List.of();
        }
    }

    /**
     * Find all social posts mentioning a specific player
     *
     * @param playerName
     * @return List of social posts with the player mentioned.
     */
    public List<SocialPost> findSocialPostsWithPlayer(String playerName) {
        try {
            return socialPostRepository.findByMentionedPlayersContaining(playerName);
        } catch (Exception e) {
            log.error("Error finding social posts with player {}: {}", playerName, e.getMessage());
            return List.of();
        }
    }

    /**
     * Aggregate the player's sentiment score across all articles and social posts
     *
     * @param playerName Name of the player to analyze
     * @return Average sentiment score (-1.0 to 1.0), or 0 if no content found
     */
    public BigDecimal aggregatePlayerSentiment(String playerName) {

        BigDecimal totalScore = BigDecimal.ZERO;
        int totalArticlesAndPosts = 0;

        List<Article> articles = findArticlesWithPlayer(playerName);
        List<SocialPost> posts = findSocialPostsWithPlayer(playerName);

        for (Article a : articles) {
            Long articleId = a.getId();

            SentimentAnalysis analysis = sentimentAnalysisRepository.findByContentTypeAndContentId("ARTICLE", articleId).orElse(null);
            if (analysis != null) {
                totalScore = totalScore.add(analysis.getSentimentScore());
                totalArticlesAndPosts++;
            }
        }

        for (SocialPost p : posts) {
            Long postId = p.getId();

            SentimentAnalysis analysis = sentimentAnalysisRepository.findByContentTypeAndContentId("SOCIAL_POST", postId).orElse(null);
            if (analysis != null) {
                totalScore = totalScore.add(analysis.getSentimentScore());
                totalArticlesAndPosts++;
            }
        }

        return totalArticlesAndPosts > 0 ? totalScore.divide(new BigDecimal(totalArticlesAndPosts), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    /**
     * Count how many times a player is mentioned across all content
     *
     * @param playerName Name of the player
     * @return Total mention count
     */
    public int countPlayerMentions(String playerName) {
        int articleCount = findArticlesWithPlayer(playerName).size();
        int postCount = findSocialPostsWithPlayer(playerName).size();
        return articleCount + postCount;
    }

    /**
     * Generates a comma-separated string of top keywords associated with a player
     * based on article and social post content analysis.
     *
     * @param playerName The player name to analyze
     * @return Comma-separated string of top keywords
     */
    private String generateTopKeywords(String playerName) {
        // Get articles and posts mentioning the player
        List<Article> articles = findArticlesWithPlayer(playerName);
        List<SocialPost> posts = findSocialPostsWithPlayer(playerName);

        // This is just a placeholder - implement your own keyword extraction logic
        // based on your application's requirements and available NLP tools
        Map<String, Integer> keywordFrequency = new HashMap<>();

        // Process articles
        for (Article article : articles) {
            extractKeywords(article.getContent(), keywordFrequency);
        }

        // Process social posts
        for (SocialPost post : posts) {
            extractKeywords(post.getContent(), keywordFrequency);
        }

        // Get top keywords (e.g., top 10)
        return keywordFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
    }

    /**
     * Extract keywords from text and update their frequency counts
     */
    private void extractKeywords(String text, Map<String, Integer> keywordFrequency) {
        // This is a simplified example. In a real application, you would:
        // 1. Use NLP techniques to extract meaningful keywords
        // 2. Filter out stopwords
        // 3. Apply stemming or lemmatization
        // 4. Consider using a library like OpenNLP, Stanford NLP, or your Python NLP service

        if (text == null || text.isEmpty()) {
            return;
        }

        // Simple implementation - split by spaces and count words
        // In a real implementation, you'd use more sophisticated NLP techniques
        String[] words = text.toLowerCase().split("\\W+");
        for (String word : words) {
            if (word.length() > 3) { // ignore very short words
                keywordFrequency.put(word, keywordFrequency.getOrDefault(word, 0) + 1);
            }
        }
    }


    /**
     * Creates a new PlayerInsight if one doesn't exist for the player,
     * or updates an existing one with the latest sentiment, mention count, and keywords.
     *
     * @param playerName The name of the player to create or update insight for
     * @return The created or updated PlayerInsight entity
     */
    @Transactional
    public PlayerInsight createOrUpdateInsight(String playerName) {
        // Check if insight already exists for this player
        Optional<PlayerInsight> existing = playerInsightRepository.findByPlayerIgnoreCase(playerName);

        // Get data to populate the insight
        BigDecimal sentiment = aggregatePlayerSentiment(playerName);
        int mentions = countPlayerMentions(playerName);
        String keywords = generateTopKeywords(playerName); // You'll need to implement this method

        if (existing.isPresent()) {
            // Update existing insight
            PlayerInsight insight = existing.get();
            insight.setAvgSentiment(sentiment);
            insight.setMentionCount(mentions);
            insight.setTopKeywords(keywords);
            return playerInsightRepository.save(insight);
        } else {
            // Create new insight
            PlayerInsight insight = new PlayerInsight();
            insight.setPlayer(playerName);
            insight.setAvgSentiment(sentiment);
            insight.setMentionCount(mentions);
            insight.setTopKeywords(keywords);
            return playerInsightRepository.save(insight);
        }
    }

    /**
     * Get a player insight
     *
     * @param playerName The name of the player to get the insight for
     * @return the player insight for the requested player
     */
    public PlayerInsight getInsight(String playerName) {
        return playerInsightRepository.findByPlayerIgnoreCase(playerName)
                .orElseThrow(() -> new ResourceNotFoundException("No insight found for player: " + playerName));
    }
}
