package com.nflp.nflp_backend.controller;

import com.nflp.nflp_backend.entity.Article;
import com.nflp.nflp_backend.entity.FetchJob;
import com.nflp.nflp_backend.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping("/fetch")
    public ResponseEntity<FetchJob> fetchNews(@RequestParam(defaultValue = "10") Integer pageSize) {
        FetchJob job = newsService.fetchNews(pageSize);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(newsService.getAllArticles());
    }

    @GetMapping("/unanalyzed")
    public ResponseEntity<List<Article>> getUnanalyzedArticles() {
        return ResponseEntity.ok(newsService.getUnanalyzedArticles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getArticleById(id));
    }
}
