package com.nflp.nflp_backend.service;

import com.nflp.nflp_backend.entity.Article;
import com.nflp.nflp_backend.entity.SentimentAnalysis;
import com.nflp.nflp_backend.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AnalysisServiceTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AnalysisService analysisService;

    @Test
    public void testAnalyzeArticle() {
        // Create test article
        Article article = new Article();
        article.setTitle("Test Article");
        article.setContent("Josh Allen threw an amazing touchdown!");
        article.setUrl("https://test.com/2");
        article.setSource("TEST");
        article.setPublishedAt(LocalDateTime.now());
        article.setAnalyzed(false);

        // Save it to the article repository
        Article saved = articleRepository.save(article);

        // Analyze it
        SentimentAnalysis analysis = analysisService.analyzeArticle(saved.getId());

        // Verify
        assertThat(analysis).isNotNull();
        assertThat(analysis.getSentimentLabel()).isIn("POSITIVE", "NEGATIVE", "NEUTRAL");
        assertThat(analysis.getContentType()).isEqualTo("ARTICLE");
        assertThat(analysis.getContentId()).isEqualTo(saved.getId());

        // Verify article updated
        Article updated = articleRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getAnalyzed()).isTrue();
    }
}