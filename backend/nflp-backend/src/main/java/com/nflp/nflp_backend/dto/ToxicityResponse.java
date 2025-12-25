package com.nflp.nflp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToxicityResponse {
    private BigDecimal toxicityScore;
    private Boolean isToxic;
    private List<String> flaggedWords;
}