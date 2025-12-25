package com.nflp.nflp_backend.dto;

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
    private List<String> allEntities;
}
