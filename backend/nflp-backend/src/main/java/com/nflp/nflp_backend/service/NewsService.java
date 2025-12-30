package com.nflp.nflp_backend.service;

import com.nflp.nflp_backend.entity.Article;
import com.nflp.nflp_backend.entity.FetchJob;
import com.nflp.nflp_backend.repository.ArticleRepository;
import com.nflp.nflp_backend.repository.FetchJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final ArticleRepository articleRepository;
    private final FetchJobRepository fetchJobRepository;
    private final RestTemplate restTemplate;

    @Value("${newsapi.key}")
    private String newsApiKey;

    private static final String NEWS_API_URL = "https://newsapi.org/v2/everything";

    public FetchJob fetchNews(Integer pageSize) {
        System.out.println("DEBUG: News API key: " + newsApiKey);

        // Create fetch job
        FetchJob job = new FetchJob();
        job.setJobType("NEWS_FETCH");
        job.setStatus("IN_PROGRESS");
        job.setStartedAt(LocalDateTime.now());
        job = fetchJobRepository.save(job);

        try {
            // Build URL
            String apiUrl = String.format(
                    "%s?q=NFL&language=en&sortBy=publishedAt&pageSize=%d&apiKey=%s",
                    NEWS_API_URL, pageSize, newsApiKey
            );

            // Call API
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

            // Get articles array from response
            List<Map<String, Object>> articles = (List<Map<String, Object>>) response.get("articles");

            // Save articles
            int savedCount = 0;

            for (Map<String, Object> apiArticle : articles) {
                String url = (String) apiArticle.get("url");

                // Skip if the article already exists
                if (!articleRepository.findByTitleContainingIgnoreCase((String) apiArticle.get("title")).isEmpty()) {
                    continue;
                }

                // Create and save article
                Article article = new Article();
                article.setTitle((String) apiArticle.get("title"));
                article.setUrl(url);

                // Get content (use description if content is null)
                String content = (String) apiArticle.get("content");
                if (content == null) {
                    content = (String) apiArticle.get("description");
                }
                article.setContent(content);

                // Get source name
                Map<String, Object> source = (Map<String, Object>) apiArticle.get("source");
                article.setSource(source != null ? (String) source.get("name") : "Unknown");

                // Parse published date
                String publishedAt = (String) apiArticle.get("publishedAt");
                if (publishedAt != null) {
                    try {
                        article.setPublishedAt(LocalDateTime.parse(publishedAt, DateTimeFormatter.ISO_DATE_TIME));
                    } catch (Exception e) {
                        article.setPublishedAt(LocalDateTime.now());
                    }
                } else {
                    article.setPublishedAt(LocalDateTime.now());
                }

                article.setFetchedAt(LocalDateTime.now());
                article.setAnalyzed(false);

                articleRepository.save(article);
                savedCount++;
            }

            // Update job - success
            job.setStatus("COMPLETED");
            job.setItemsFetched(savedCount);
            job.setCompletedAt(LocalDateTime.now());
            fetchJobRepository.save(job);

            return job;

        } catch (Exception e) {
            // Update job - failure
            job.setStatus("FAILED");
            job.setErrorMessage(e.getMessage());
            job.setCompletedAt(LocalDateTime.now());
            fetchJobRepository.save(job);

            throw new RuntimeException("Failed to fetch news: " + e.getMessage());
        }
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> getUnanalyzedArticles() {
        return articleRepository.findByAnalyzedFalse();
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    public long countUnanalyzedArticles() {
        return articleRepository.countByAnalyzedFalse();
    }
}