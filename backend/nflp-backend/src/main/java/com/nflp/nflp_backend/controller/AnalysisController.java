package com.nflp.nflp_backend.controller;

import com.nflp.nflp_backend.dto.SentimentResponse;
import com.nflp.nflp_backend.entity.SentimentAnalysis;
import com.nflp.nflp_backend.dto.BatchAnalysisRequest;
import com.nflp.nflp_backend.dto.BatchAnalysisResult;
import com.nflp.nflp_backend.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/analyzeArticle")
    public ResponseEntity<SentimentAnalysis> analyzeArticle(@RequestParam Long articleId) {
        SentimentAnalysis analysis = analysisService.analyzeArticle(articleId);
        return ResponseEntity.ok(analysis);
    }

    @PostMapping("/analyzePost")
    public ResponseEntity<SentimentAnalysis> analyzePost(@RequestParam Long postId) {
        SentimentAnalysis analysis = analysisService.analyzePost(postId);
        return ResponseEntity.ok(analysis);
    }

    @PostMapping("/batchAnalyze")
    public ResponseEntity<BatchAnalysisResult> batchAnalyze(@RequestParam List<Long> articleIds, @RequestParam List<Long> postIds) {
        BatchAnalysisRequest request = new BatchAnalysisRequest();
        request.setArticleIds(articleIds);
        request.setPostIds(postIds);
        BatchAnalysisResult result = analysisService.batchAnalyze(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get")
    public ResponseEntity<SentimentAnalysis> getAnalysis(@RequestParam String contentType, @RequestParam Long contentId) {
        SentimentAnalysis analysis = analysisService.getAnalysis(contentType, contentId);
        return ResponseEntity.ok(analysis);
    }

    @DeleteMapping("/delete")
    public void deleteAnalysis(@RequestParam String contentType, @RequestParam Long contentId) {
        analysisService.deleteAnalysis(contentType, contentId);
    }

    @GetMapping("/getToxicContent")
    public ResponseEntity<List<SentimentAnalysis>> getToxicContent(@RequestParam BigDecimal threshold) {
        List<SentimentAnalysis> analysis = analysisService.getToxicContent(threshold);
        return ResponseEntity.ok(analysis);
    }
}
