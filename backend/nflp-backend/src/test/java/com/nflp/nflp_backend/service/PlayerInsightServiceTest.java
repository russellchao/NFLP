package com.nflp.nflp_backend.service;

import com.nflp.nflp_backend.entity.Article;
import com.nflp.nflp_backend.entity.PlayerInsight;
import com.nflp.nflp_backend.entity.SentimentAnalysis;
import com.nflp.nflp_backend.entity.SocialPost;
import com.nflp.nflp_backend.repository.ArticleRepository;
import com.nflp.nflp_backend.repository.PlayerInsightRepository;
import com.nflp.nflp_backend.repository.SentimentAnalysisRepository;
import com.nflp.nflp_backend.repository.SocialPostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class PlayerInsightServiceTest {

    @Autowired
    private PlayerInsightService playerInsightService;

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private PlayerInsightRepository playerInsightRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SocialPostRepository socialPostRepository;

    @Autowired
    private SentimentAnalysisRepository sentimentAnalysisRepository;

    @Test
    public void testGeneratePlayerInsight() {

        // Analyze the sentiment of articles and social posts about Josh Allen
        ArticleSentimentAnalysis();
        SocialPostSentimentAnalysis();

        // Generate an insight about Josh Allen
        playerInsightService.createOrUpdateInsight("Josh Allen");

        // Retrieve the insight about Josh Allen from the player insight repo
        PlayerInsight insight = playerInsightRepository.findByPlayerIgnoreCase("josh allen").get();

        // Debug Statements about the insight
        System.out.println("Player: " + insight.getPlayer());
        System.out.println("Average Sentiment: " + insight.getAvgSentiment());
        System.out.println("Total mentions: " + insight.getMentionCount());
        System.out.println("Top keywords: " + insight.getTopKeywords());
    }

    void ArticleSentimentAnalysis() {
        // Create articles about Josh Allen
        Article article1 = new Article();
        article1.setTitle("Test Article 1");
        article1.setContent("Josh Allen threw an amazing touchdown!");
        article1.setUrl("https://test.com/joshallen/1");
        article1.setSource("TEST");
        article1.setPublishedAt(LocalDateTime.now());
        article1.setAnalyzed(false);

        Article article2 = new Article();
        article2.setTitle("Test Article 2");
        article2.setContent("Josh Allen led the Bills to victory!");
        article2.setUrl("https://test.com/joshallen/2");
        article2.setSource("TEST");
        article2.setPublishedAt(LocalDateTime.now());
        article2.setAnalyzed(false);

        // Save the articles to the article repo
        Article savedArticle1 = articleRepository.save(article1);
        Article savedArticle2 = articleRepository.save(article2);

        // Analyze the sentiment of the articles
        SentimentAnalysis analysis1 = analysisService.analyzeArticle(savedArticle1.getId());
        SentimentAnalysis analysis2 = analysisService.analyzeArticle(savedArticle2.getId());

        // Save the sentiment analyses to the sentiment analysis repo
        sentimentAnalysisRepository.save(analysis1);
        sentimentAnalysisRepository.save(analysis2);
    }

    void SocialPostSentimentAnalysis() {
        // Create social posts about Josh Allen
        SocialPost post1 = new SocialPost();
        post1.setPlatform("TWITTER");
        post1.setPostId("PostId1");
        post1.setContent("Josh Allen outran so many defenders for a touchdown!");
        post1.setAuthor("Author1");
        post1.setPostedAt(LocalDateTime.now());
        post1.setSubreddit("N/A");

        SocialPost post2 = new SocialPost();
        post2.setPlatform("REDDIT");
        post2.setPostId("PostId2");
        post2.setContent("Josh Allen nearly lost the Bills the game with that interception!");
        post2.setAuthor("Author2");
        post2.setPostedAt(LocalDateTime.now());
        post2.setSubreddit("r/nfl");

        // Save the posts to the social post repo
        SocialPost savedPost1 = socialPostRepository.save(post1);
        SocialPost savedPost2 = socialPostRepository.save(post2);

        // Analyze the sentiment of the posts
        SentimentAnalysis analysis1 = analysisService.analyzePost(savedPost1.getId());
        SentimentAnalysis analysis2 = analysisService.analyzePost(savedPost2.getId());

        // Save the sentiment analyses to the sentiment analysis repo
        sentimentAnalysisRepository.save(analysis1);
        sentimentAnalysisRepository.save(analysis2);
    }
}
