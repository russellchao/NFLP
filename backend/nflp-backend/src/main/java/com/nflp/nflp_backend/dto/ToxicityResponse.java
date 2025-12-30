package com.nflp.nflp_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToxicityResponse {
    @JsonProperty("toxicity_score")
    private BigDecimal toxicityScore;

    @JsonProperty("is_toxic")
    private Boolean isToxic;

    @JsonProperty("flagged_words")
    private List<String> flaggedWords;
}