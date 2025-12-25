package com.nflp.nflp_backend.repository;

import com.nflp.nflp_backend.entity.FetchJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FetchJobRepository extends JpaRepository<FetchJob, Long> {

    // Find by status
    List<FetchJob> findByStatus(String status);

    // Find by job type
    List<FetchJob> findByJobType(String jobType);

    // Find by job type and status
    List<FetchJob> findByJobTypeAndStatus(String jobType, String status);

    // Find recent jobs (last N)
    List<FetchJob> findTop10ByOrderByStartedAtDesc();

    // Find jobs started after a certain date
    List<FetchJob> findByStartedAtAfter(LocalDateTime date);

    // Find completed jobs
    List<FetchJob> findByStatusOrderByCompletedAtDesc(String status);

    // Find failed jobs
    @Query("SELECT f FROM FetchJob f WHERE f.status = 'FAILED' ORDER BY f.startedAt DESC")
    List<FetchJob> findFailedJobs();

    // Find in-progress jobs
    @Query("SELECT f FROM FetchJob f WHERE f.status = 'IN_PROGRESS' ORDER BY f.startedAt DESC")
    List<FetchJob> findInProgressJobs();

    // Find jobs by type in date range
    List<FetchJob> findByJobTypeAndStartedAtBetween(
            String jobType,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Count by status
    long countByStatus(String status);

    // Count by job type
    long countByJobType(String jobType);

    // Get total items fetched by job type
    @Query("SELECT SUM(f.itemsFetched) FROM FetchJob f WHERE f.jobType = :jobType AND f.status = 'COMPLETED'")
    Long getTotalItemsFetchedByType(@Param("jobType") String jobType);

    // Get average items fetched per job
    @Query("SELECT AVG(f.itemsFetched) FROM FetchJob f WHERE f.status = 'COMPLETED'")
    Double getAverageItemsFetched();

    // Find most recent job by type
    @Query("SELECT f FROM FetchJob f WHERE f.jobType = :jobType ORDER BY f.startedAt DESC LIMIT 1")
    FetchJob findMostRecentByJobType(@Param("jobType") String jobType);

    // Delete old completed jobs (cleanup)
    void deleteByStatusAndCompletedAtBefore(String status, LocalDateTime date);
}
