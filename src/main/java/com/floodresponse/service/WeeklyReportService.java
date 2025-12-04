package com.floodresponse.service;

import com.floodresponse.model.WeeklyReport;
import com.floodresponse.repository.WeeklyReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WeeklyReportService {
    
    @Autowired
    private WeeklyReportRepository weeklyReportRepository;
    
    /**
     * Get the latest reports for all provinces
     */
    public List<WeeklyReport> getLatestReports() {
        // Get the most recent week start date
        LocalDate now = LocalDate.now();
        LocalDate currentWeekStart = now.with(java.time.DayOfWeek.MONDAY);
        
        // Try to get current week's reports first
        List<WeeklyReport> reports = weeklyReportRepository.findByWeekStartDateOrderByProvinceAsc(currentWeekStart);
        
        // If no reports for current week, get the 5 most recent
        if (reports.isEmpty()) {
            reports = weeklyReportRepository.findTop5ByOrderByGeneratedAtDesc();
        }
        
        return reports;
    }
    
    /**
     * Get all reports for a specific week
     */
    public List<WeeklyReport> getReportsByWeek(LocalDate weekStart) {
        return weeklyReportRepository.findByWeekStartDateOrderByProvinceAsc(weekStart);
    }
    
    /**
     * Get a specific report for a province and week
     */
    public Optional<WeeklyReport> getReportByProvinceAndWeek(String province, LocalDate weekStart) {
        return weeklyReportRepository.findByProvinceAndWeekStartDate(province, weekStart);
    }
    
    /**
     * Get all historical reports for a province
     */
    public List<WeeklyReport> getAllReportsForProvince(String province) {
        return weeklyReportRepository.findByProvinceOrderByWeekStartDateDesc(province);
    }
    
    /**
     * Save a weekly report
     */
    public WeeklyReport saveReport(WeeklyReport report) {
        return weeklyReportRepository.save(report);
    }
    
    /**
     * Check if a report exists for a province and week
     */
    public boolean reportExists(String province, LocalDate weekStart) {
        return weeklyReportRepository.findByProvinceAndWeekStartDate(province, weekStart).isPresent();
    }
}
