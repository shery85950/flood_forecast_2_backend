package com.floodresponse.controller;

import com.floodresponse.model.WeeklyReport;
import com.floodresponse.service.WeeklyReportSchedulerService;
import com.floodresponse.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weekly-reports")
@CrossOrigin(origins = "*")
public class WeeklyReportController {
    
    @Autowired
    private WeeklyReportService weeklyReportService;
    
    @Autowired
    private WeeklyReportSchedulerService schedulerService;
    
    /**
     * Get the latest reports for all provinces
     * GET /api/weekly-reports/latest
     */
    @GetMapping("/latest")
    public ResponseEntity<List<WeeklyReport>> getLatestReports() {
        List<WeeklyReport> reports = weeklyReportService.getLatestReports();
        return ResponseEntity.ok(reports);
    }
    
    /**
     * Get reports for a specific week
     * GET /api/weekly-reports/week/2025-12-02
     */
    @GetMapping("/week/{date}")
    public ResponseEntity<List<WeeklyReport>> getReportsByWeek(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // Ensure the date is a Monday
        LocalDate weekStart = date.with(java.time.DayOfWeek.MONDAY);
        List<WeeklyReport> reports = weeklyReportService.getReportsByWeek(weekStart);
        return ResponseEntity.ok(reports);
    }
    
    /**
     * Get all reports for a specific province
     * GET /api/weekly-reports/province/Punjab
     */
    @GetMapping("/province/{province}")
    public ResponseEntity<List<WeeklyReport>> getProvinceReports(@PathVariable String province) {
        List<WeeklyReport> reports = weeklyReportService.getAllReportsForProvince(province);
        return ResponseEntity.ok(reports);
    }
    
    /**
     * Manually trigger report generation (admin use)
     * POST /api/weekly-reports/generate
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateReports() {
        try {
            schedulerService.generateWeeklyReports();
            return ResponseEntity.ok("{\"message\": \"Weekly reports generation triggered successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
