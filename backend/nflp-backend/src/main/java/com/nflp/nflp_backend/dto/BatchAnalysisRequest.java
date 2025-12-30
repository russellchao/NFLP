package com.nflp.nflp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchAnalysisRequest {
    private List<Long> articleIds;
    private List<Long> postIds;
    private Boolean analyzeSentiment = true;
    private Boolean analyzeToxicity = true;
    private Boolean extractEntities = true;
}