package com.floodresponse.repository;

import com.floodresponse.model.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {
    
    /**
     * Find a specific week's report for a province
     */
    Optional<WeeklyReport> findByProvinceAndWeekStartDate(String province, LocalDate weekStartDate);
    
    /**
     * Get all provinces' reports for a specific week
     */
    List<WeeklyReport> findByWeekStartDateOrderByProvinceAsc(LocalDate weekStartDate);
    
    /**
     * Get the latest report for a specific province
     */
    Optional<WeeklyReport> findTopByProvinceOrderByGeneratedAtDesc(String province);
    
    /**
     * Get the 5 most recent reports (one per province ideally)
     */
    List<WeeklyReport> findTop5ByOrderByGeneratedAtDesc();
    
    /**
     * Get all reports for a specific province
     */
    List<WeeklyReport> findByProvinceOrderByWeekStartDateDesc(String province);
}
