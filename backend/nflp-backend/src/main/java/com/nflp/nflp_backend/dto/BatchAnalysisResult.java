package com.nflp.nflp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchAnalysisResult {
    private Integer totalRequested;
    private Integer articlesAnalyzed;
    private Integer postsAnalyzed;
    private Integer failed;
    private List<String> errors = new ArrayList<>();
    private String processingTime;
}
