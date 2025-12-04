package com.floodresponse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floodresponse.dto.WeatherForecastRequest;
import com.floodresponse.model.WeeklyReport;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WeeklyReportSchedulerService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeeklyReportSchedulerService.class);
    
    private static final String WEATHER_API_KEY = "5523bf8add464255b93210055252911";
    private static final String WEATHER_API_BASE = "https://api.weatherapi.com/v1";
    
    // Province to City mapping
    private static final Map<String, String> PROVINCE_CITIES = Map.of(
        "Punjab", "Lahore",
        "Sindh", "Karachi",
        "KPK", "Peshawar",
        "Balochistan", "Quetta",
        "Gilgit-Baltistan", "Gilgit"
    );
    
    @Autowired
    private WeeklyReportService weeklyReportService;
    
    @Autowired
    private AIForecastService aiForecastService;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Run immediately on startup to check if current week needs a report
     */
    @PostConstruct
    public void initializeReports() {
        logger.info("Checking if current week needs report generation...");
        
        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.with(DayOfWeek.MONDAY);
        
        // Check if we already have reports for this week
        List<WeeklyReport> existingReports = weeklyReportService.getReportsByWeek(weekStart);
        
        if (existingReports.isEmpty()) {
            logger.info("No reports found for current week. Generating reports now...");
            generateWeeklyReports();
        } else {
            logger.info("Reports already exist for current week ({}). Skipping generation.", weekStart);
        }
    }
    
    /**
     * Scheduled to run every Monday at 12:00 AM (midnight)
     * Cron format: second minute hour day month weekday
     * "0 0 0 * * MON" = 0 seconds, 0 minutes, 0 hours, any day, any month, Monday
     */
    @Scheduled(cron = "0 0 0 * * MON")
    public void scheduledWeeklyReportGeneration() {
        logger.info("Scheduled weekly report generation triggered at {}", LocalDateTime.now());
        generateWeeklyReports();
    }
    
    /**
     * Generate weekly reports for all 5 provinces
     */
    public void generateWeeklyReports() {
        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6); // Sunday
        
        logger.info("Generating weekly reports for week: {} to {}", weekStart, weekEnd);
        
        int successCount = 0;
        int failureCount = 0;
        
        for (Map.Entry<String, String> entry : PROVINCE_CITIES.entrySet()) {
            String province = entry.getKey();
            String city = entry.getValue();
            
            try {
                logger.info("Generating report for {} ({})", province, city);
                
                // Check if report already exists
                if (weeklyReportService.reportExists(province, weekStart)) {
                    logger.info("Report already exists for {} this week. Skipping.", province);
                    continue;
                }
                
                // Fetch 7-day weather forecast
                WeatherForecastRequest weatherData = fetchWeatherForecast(city);
                
                // Generate AI analysis
                String aiResponse = aiForecastService.analyzeForecast(weatherData);
                
                // Parse AI response
                Map<String, Object> analysis = parseAIResponse(aiResponse);
                
                // Create and save report
                WeeklyReport report = createReport(province, city, weekStart, weekEnd, analysis);
                weeklyReportService.saveReport(report);
                
                logger.info("Successfully generated report for {}", province);
                successCount++;
                
            } catch (Exception e) {
                logger.error("Failed to generate report for {}: {}", province, e.getMessage(), e);
                failureCount++;
            }
        }
        
        logger.info("Weekly report generation completed. Success: {}, Failures: {}", successCount, failureCount);
    }
    
    /**
     * Fetch 7-day weather forecast for a city
     */
    private WeatherForecastRequest fetchWeatherForecast(String city) {
        try {
            String url = String.format("%s/forecast.json?key=%s&q=%s&days=7&aqi=no&alerts=yes",
                    WEATHER_API_BASE, WEATHER_API_KEY, city);
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response == null) {
                throw new RuntimeException("Empty response from weather API");
            }
            
            // Extract location data
            Map<String, Object> locationData = (Map<String, Object>) response.get("location");
            WeatherForecastRequest.Location location = new WeatherForecastRequest.Location();
            location.setName((String) locationData.get("name"));
            location.setRegion((String) locationData.get("region"));
            location.setCountry((String) locationData.get("country"));
            
            // Extract forecast data
            Map<String, Object> forecastData = (Map<String, Object>) response.get("forecast");
            List<Map<String, Object>> forecastDays = (List<Map<String, Object>>) forecastData.get("forecastday");
            
            List<WeatherForecastRequest.ForecastDay> forecast = new ArrayList<>();
            for (Map<String, Object> dayData : forecastDays) {
                Map<String, Object> day = (Map<String, Object>) dayData.get("day");
                Map<String, Object> condition = (Map<String, Object>) day.get("condition");
                
                WeatherForecastRequest.ForecastDay forecastDay = new WeatherForecastRequest.ForecastDay();
                forecastDay.setDate((String) dayData.get("date"));
                forecastDay.setMaxTemp(((Number) day.get("maxtemp_c")).doubleValue());
                forecastDay.setMinTemp(((Number) day.get("mintemp_c")).doubleValue());
                forecastDay.setAvgTemp(((Number) day.get("avgtemp_c")).doubleValue());
                forecastDay.setTotalRainfall(((Number) day.get("totalprecip_mm")).doubleValue());
                forecastDay.setAvgHumidity(((Number) day.get("avghumidity")).doubleValue());
                forecastDay.setRainChance(((Number) day.get("daily_chance_of_rain")).doubleValue());
                forecastDay.setCondition((String) condition.get("text"));
                
                forecast.add(forecastDay);
            }
            
            WeatherForecastRequest request = new WeatherForecastRequest();
            request.setLocation(location);
            request.setForecast(forecast);
            
            return request;
            
        } catch (Exception e) {
            logger.error("Error fetching weather forecast for {}: {}", city, e.getMessage());
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage());
        }
    }
    
    /**
     * Parse AI response JSON
     */
    private Map<String, Object> parseAIResponse(String aiResponse) {
        try {
            return objectMapper.readValue(aiResponse, Map.class);
        } catch (Exception e) {
            logger.error("Error parsing AI response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse AI response: " + e.getMessage());
        }
    }
    
    /**
     * Create WeeklyReport entity from AI analysis
     */
    private WeeklyReport createReport(String province, String city, LocalDate weekStart, 
                                     LocalDate weekEnd, Map<String, Object> analysis) {
        WeeklyReport report = new WeeklyReport();
        report.setProvince(province);
        report.setCity(city);
        report.setWeekStartDate(weekStart);
        report.setWeekEndDate(weekEnd);
        report.setWarningLevel((String) analysis.get("warningLevel"));
        report.setRiskScore((Integer) analysis.get("riskScore"));
        report.setConfidence((Integer) analysis.get("confidence"));
        report.setSummary((String) analysis.get("summary"));
        
        try {
            // Convert lists to JSON strings
            report.setKeyFactors(objectMapper.writeValueAsString(analysis.get("keyFactors")));
            report.setRecommendations(objectMapper.writeValueAsString(analysis.get("recommendations")));
            report.setDailyRisks(objectMapper.writeValueAsString(analysis.get("dailyRisks")));
            report.setPeakRiskDays(objectMapper.writeValueAsString(analysis.get("peakRiskDays")));
        } catch (Exception e) {
            logger.error("Error serializing JSON fields: {}", e.getMessage());
        }
        
        report.setGeneratedAt(LocalDateTime.now());
        
        return report;
    }
}
