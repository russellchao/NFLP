package com.nflp.nflp_backend.repository;

import com.nflp.nflp_backend.entity.Player;
import com.nflp.nflp_backend.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    // Find by name
    Optional<Player> findByName(String name);

    // Find by team
    List<Player> findByTeam(Team team);

    // Find by team id
    List<Player> findByTeamId(Long teamId);

    // Find by position
    List<Player> findByPosition(String position);

    // Search by name containing
    List<Player> findByNameContainingIgnoreCase(String keyword);

    // Find by team and position
    List<Player> findByTeamIdAndPosition(Long teamId, String position);

    // Find all ordered by name
    List<Player> findAllByOrderByNameAsc();

    // Check if player exists by name
    boolean existsByName(String name);
}