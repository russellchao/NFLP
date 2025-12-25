package com.nflp.nflp_backend.repository;

import com.nflp.nflp_backend.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // Find by name
    Optional<Team> findByName(String name);

    // Find by abbreviation
    Optional<Team> findByAbbreviation(String abbreviation);

    // Find by conference
    List<Team> findByConference(String conference);

    // Find by division
    List<Team> findByDivision(String division);

    // Find by conference and division
    List<Team> findByConferenceAndDivision(String conference, String division);

    // Search by name containing
    List<Team> findByNameContainingIgnoreCase(String keyword);

    // Find all ordered by name
    List<Team> findAllByOrderByNameAsc();
}