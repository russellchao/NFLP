package com.nflp.nflp_backend.controller;

import com.nflp.nflp_backend.repository.ArticleRepository;
import com.nflp.nflp_backend.repository.PlayerInsightRepository;
import com.nflp.nflp_backend.repository.SentimentAnalysisRepository;
import com.nflp.nflp_backend.repository.SocialPostRepository;
import com.nflp.nflp_backend.service.AnalysisService;
import com.nflp.nflp_backend.service.PlayerInsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ArticleRepository articleRepository;
    private final SocialPostRepository socialPostRepository;
    private final SentimentAnalysisRepository sentimentAnalysisRepository;
    private final PlayerInsightRepository playerInsightRepository;

    @GetMapping("/num-articles-analyzed")
    public Long getNumArticlesAnalyzed() {
        return articleRepository.countByAnalyzedTrue();
    }

    @GetMapping("/num-posts-analyzed")
    public Long getNumPostsAnalyzed() {
        return socialPostRepository.countByAnalyzedTrue();
    }

    @GetMapping("/players-tracked")
    public Long getNumPlayersTracked() {
        return playerInsightRepository.count();
    }

    @GetMapping("/avg-sentiment")
    public BigDecimal getAvgSentiment() {
        return sentimentAnalysisRepository.getAverageSentiment();
    }
}
