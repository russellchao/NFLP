package com.nflp.nflp_backend.service;

import com.nflp.nflp_backend.dto.*;
import com.nflp.nflp_backend.entity.Article;
import com.nflp.nflp_backend.entity.SentimentAnalysis;
import com.nflp.nflp_backend.entity.SocialPost;
import com.nflp.nflp_backend.exception.ResourceNotFoundException;
import com.nflp.nflp_backend.repository.ArticleRepository;
import com.nflp.nflp_backend.repository.SentimentAnalysisRepository;
import com.nflp.nflp_backend.repository.SocialPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final NLPClientService nlpClientService;
    private final ArticleRepository articleRepository;
    private final SocialPostRepository socialPostRepository;
    private final SentimentAnalysisRepository sentimentAnalysisRepository;

    /**
     * Analyze an article for sentiment, entities, and toxicity
     *
     * @param articleId ID of article to analyze
     * @return SentimentAnalysis entity
     */
    @Transactional
    public SentimentAnalysis analyzeArticle(Long articleId) {
        log.info("Analyzing article with ID: {}", articleId);

        // Find the article
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID: " + articleId));

        // Check if already analyzed
        Optional<SentimentAnalysis> existing = sentimentAnalysisRepository
                .findByContentTypeAndContentId("ARTICLE", articleId);

        if (existing.isPresent()) {
            log.warn("Article {} already analyzed", articleId);
            return existing.get();
        }

        // Call NLP service
        SentimentResponse sentiment = nlpClientService.analyzeSentiment(article.getTitle() + article.getContent());
        EntityResponse entities = nlpClientService.extractEntities(article.getTitle() + article.getContent());
        ToxicityResponse toxicity = nlpClientService.detectToxicity(article.getTitle() + article.getContent());

        // DEBUG CHECKPOINT
        System.out.println("toxicity: " + toxicity.getToxicityScore());
        System.out.println("entities: " + entities.getAllEntities());
        System.out.println("sentiment: " + sentiment.getLabel());

        // Create sentiment analysis entity
        SentimentAnalysis analysis = new SentimentAnalysis();
        analysis.setContentType("ARTICLE");
        analysis.setContentId(articleId);
        analysis.setSentimentScore(sentiment.getScore());
        analysis.setSentimentLabel(sentiment.getLabel());
        analysis.setPositive(sentiment.getPositive());
        analysis.setNegative(sentiment.getNegative());
        analysis.setNeutral(sentiment.getNeutral());
        analysis.setCompound(sentiment.getCompound());
        analysis.setToxicityScore(toxicity.getToxicityScore());

        // Store extracted entities as comma-separated strings
        analysis.setExtractedEntities(String.join(",", entities.getAllEntities()));

        // Save analysis
        SentimentAnalysis saved = sentimentAnalysisRepository.save(analysis);

        // Update article
        article.setAnalyzed(true);
        article.setMentionedPlayers(String.join(",", entities.getPlayers()));
        article.setMentionedTeams(String.join(",", entities.getTeams()));
        articleRepository.save(article);

        log.info("Article {} analyzed successfully: {}", articleId, sentiment.getLabel());

        return saved;
    }

    /**
     * Analyze a social post for sentiment, entities, and toxicity
     *
     * @param postId ID of post to analyze
     * @return SentimentAnalysis entity
     */
    @Transactional
    public SentimentAnalysis analyzePost(Long postId) {
        log.info("Analyzing social post with ID: {}", postId);

        // Find the post
        SocialPost post = socialPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Social post not found with ID: " + postId));

        // Check if already analyzed
        Optional<SentimentAnalysis> existing = sentimentAnalysisRepository
                .findByContentTypeAndContentId("SOCIAL_POST", postId);

        if (existing.isPresent()) {
            log.warn("Post {} already analyzed", postId);
            return existing.get();
        }

        // Call NLP service
        SentimentResponse sentiment = nlpClientService.analyzeSentiment(post.getContent());
        EntityResponse entities = nlpClientService.extractEntities(post.getContent());
        ToxicityResponse toxicity = nlpClientService.detectToxicity(post.getContent());

        // Create sentiment analysis entity
        SentimentAnalysis analysis = new SentimentAnalysis();
        analysis.setContentType("SOCIAL_POST");
        analysis.setContentId(postId);
        analysis.setSentimentScore(sentiment.getScore());
        analysis.setSentimentLabel(sentiment.getLabel());
        analysis.setPositive(sentiment.getPositive());
        analysis.setNegative(sentiment.getNegative());
        analysis.setNeutral(sentiment.getNeutral());
        analysis.setCompound(sentiment.getCompound());
        analysis.setToxicityScore(toxicity.getToxicityScore());

        // Store extracted entities
        analysis.setExtractedEntities(String.join(",", entities.getAllEntities()));

        // Save analysis
        SentimentAnalysis saved = sentimentAnalysisRepository.save(analysis);

        // Update post
        post.setAnalyzed(true);
        post.setMentionedPlayers(String.join(",", entities.getPlayers()));
        post.setMentionedTeams(String.join(",", entities.getTeams()));
        socialPostRepository.save(post);

        log.info("Post {} analyzed successfully: {}", postId, sentiment.getLabel());

        return saved;
    }

    /**
     * Batch analyze multiple articles and posts
     *
     * @param request Batch analysis request with article and post IDs
     * @return BatchAnalysisResult with statistics
     */
    @Transactional
    public BatchAnalysisResult batchAnalyze(BatchAnalysisRequest request) {
        log.info("Starting batch analysis: {} articles, {} posts",
                request.getArticleIds().size(),
                request.getPostIds().size());

        long startTime = System.currentTimeMillis();

        BatchAnalysisResult result = new BatchAnalysisResult();
        result.setTotalRequested(request.getArticleIds().size() + request.getPostIds().size());
        result.setArticlesAnalyzed(0);
        result.setPostsAnalyzed(0);
        result.setFailed(0);
        result.setErrors(new ArrayList<>());

        // Analyze articles
        for (Long articleId : request.getArticleIds()) {
            try {
                analyzeArticle(articleId);
                result.setArticlesAnalyzed(result.getArticlesAnalyzed() + 1);
            } catch (Exception e) {
                log.error("Failed to analyze article {}: {}", articleId, e.getMessage());
                result.setFailed(result.getFailed() + 1);
                result.getErrors().add("Article " + articleId + ": " + e.getMessage());
            }
        }

        // Analyze posts
        for (Long postId : request.getPostIds()) {
            try {
                analyzePost(postId);
                result.setPostsAnalyzed(result.getPostsAnalyzed() + 1);
            } catch (Exception e) {
                log.error("Failed to analyze post {}: {}", postId, e.getMessage());
                result.setFailed(result.getFailed() + 1);
                result.getErrors().add("Post " + postId + ": " + e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        result.setProcessingTime((endTime - startTime) / 1000.0 + "s");

        log.info("Batch analysis complete: {} articles, {} posts, {} failed",
                result.getArticlesAnalyzed(),
                result.getPostsAnalyzed(),
                result.getFailed());

        return result;
    }

    /**
     * Get analysis for specific content
     *
     * @param contentType "ARTICLE" or "SOCIAL_POST"
     * @param contentId ID of the content
     * @return SentimentAnalysis if exists
     */
    public SentimentAnalysis getAnalysis(String contentType, Long contentId) {
        return sentimentAnalysisRepository.findByContentTypeAndContentId(contentType, contentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No analysis found for " + contentType + " with ID: " + contentId
                ));
    }

    /**
     * Delete analysis (allows re-analysis)
     *
     * @param contentType "ARTICLE" or "SOCIAL_POST"
     * @param contentId ID of the content
     */
    @Transactional
    public void deleteAnalysis(String contentType, Long contentId) {
        log.info("Deleting analysis for {} with ID: {}", contentType, contentId);

        SentimentAnalysis analysis = getAnalysis(contentType, contentId);
        sentimentAnalysisRepository.delete(analysis);

        // Update analyzed flag and nullify mentioned players and teams
        if ("ARTICLE".equals(contentType)) {
            articleRepository.findById(contentId).ifPresent(article -> {
                article.setAnalyzed(false);
                article.setMentionedPlayers(null);
                article.setMentionedTeams(null);
                articleRepository.save(article);
            });
        } else if ("SOCIAL_POST".equals(contentType)) {
            socialPostRepository.findById(contentId).ifPresent(post -> {
                post.setAnalyzed(false);
                post.setMentionedPlayers(null);
                post.setMentionedTeams(null);
                socialPostRepository.save(post);
            });
        }

        log.info("Analysis deleted for {} {}", contentType, contentId);
    }

    /**
     * Get all toxic content above threshold
     *
     * @param threshold Minimum toxicity score (0.0 to 1.0)
     * @return List of toxic content analysis
     */
    public List<SentimentAnalysis> getToxicContent(BigDecimal threshold) {
        log.info("Fetching toxic content with threshold: {}", threshold);
        return sentimentAnalysisRepository.findToxicContent(threshold);
    }

    /**
     * Get sentiment statistics
     *
     * @return Map with sentiment breakdown
     */
//    public SentimentStats getSentimentStats() {
//        long positive = sentimentAnalysisRepository.countBySentimentLabel("POSITIVE");
//        long negative = sentimentAnalysisRepository.countBySentimentLabel("NEGATIVE");
//        long neutral = sentimentAnalysisRepository.countBySentimentLabel("NEUTRAL");
//        long total = positive + negative + neutral;
//
//        BigDecimal avgSentiment = sentimentAnalysisRepository.getAverageSentimentByType("ARTICLE");
//        BigDecimal avgToxicity = sentimentAnalysisRepository.getAverageToxicity();
//
//        return new SentimentStats(
//                total,
//                positive,
//                negative,
//                neutral,
//                avgSentiment != null ? avgSentiment : BigDecimal.ZERO,
//                avgToxicity != null ? avgToxicity : BigDecimal.ZERO
//        );
//    }
}