package com.nflp.nflp_backend.repository;

import com.nflp.nflp_backend.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testSaveAndFindArticle() {
        // Create test article
        Article article = new Article();
        article.setTitle("Test Article");
        article.setContent("Test content");
        article.setUrl("https://test.com/article1");
        article.setSource("ESPN");
        article.setPublishedAt(LocalDateTime.now());
        article.setAnalyzed(false);

        // Save
        Article saved = articleRepository.save(article);

        // Verify
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Article");

        // Find unanalyzed
        var unanalyzed = articleRepository.findByAnalyzedFalse();
        assertThat(unanalyzed).isNotEmpty();
    }
}