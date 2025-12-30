package com.nflp.nflp_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityResponse {
    private List<String> players;

    private List<String> teams;

    @JsonProperty("all_entities")
    private List<String> allEntities;
}
