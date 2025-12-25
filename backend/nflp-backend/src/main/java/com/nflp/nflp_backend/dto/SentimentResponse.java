package com.nflp.nflp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResponse {
    private BigDecimal score;      // Compound score (-1 to 1)
    private String label;           // POSITIVE, NEGATIVE, NEUTRAL
    private BigDecimal positive;    // Positive score (0 to 1)
    private BigDecimal negative;    // Negative score (0 to 1)
    private BigDecimal neutral;     // Neutral score (0 to 1)
    private BigDecimal compound;    // Same as score
}