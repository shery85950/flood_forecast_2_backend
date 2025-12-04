package com.floodresponse.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "weekly_reports")
@Data
public class WeeklyReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String province;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private LocalDate weekStartDate;
    
    @Column(nullable = false)
    private LocalDate weekEndDate;
    
    @Column(nullable = false)
    private String warningLevel; // Low, Moderate, High, Critical
    
    @Column(nullable = false)
    private Integer riskScore; // 0-100
    
    @Column(nullable = false)
    private Integer confidence; // AI confidence percentage
    
    @Column(columnDefinition = "TEXT")
    private String summary;
    
    @Column(columnDefinition = "TEXT")
    private String keyFactors; // JSON array stored as string
    
    @Column(columnDefinition = "TEXT")
    private String recommendations; // JSON array stored as string
    
    @Column(columnDefinition = "TEXT")
    private String dailyRisks; // JSON array stored as string
    
    @Column(columnDefinition = "TEXT")
    private String peakRiskDays; // JSON array stored as string
    
    @Column(nullable = false)
    private LocalDateTime generatedAt;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (generatedAt == null) {
            generatedAt = LocalDateTime.now();
        }
    }
}
