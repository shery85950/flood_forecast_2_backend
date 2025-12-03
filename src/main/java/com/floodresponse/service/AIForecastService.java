package com.floodresponse.service;

import com.floodresponse.dto.WeatherForecastRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class AIForecastService {

    private static final String HF_API_URL = "https://api-inference.huggingface.co/models/SentientAGI/Dobby-Unhinged-Llama-3.3-70B";
    private static final String HF_API_KEY = "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";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analyzeForecast(WeatherForecastRequest request) {
        try {
            String prompt = createAnalysisPrompt(request);
            
            // Prepare request payload for Hugging Face Inference API
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", prompt);
            
            // Set up headers with Bearer token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(HF_API_KEY);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Make request to Hugging Face Inference API
            ResponseEntity<String> response = restTemplate.postForEntity(HF_API_URL, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Parse response - Inference API returns array of objects
                List<Map<String, Object>> responseList = objectMapper.readValue(
                    response.getBody(), 
                    List.class
                );
                
                if (!responseList.isEmpty()) {
                    Map<String, Object> result = responseList.get(0);
                    String generatedText = (String) result.get("generated_text");
                    
                    // Clean up the response (remove prompt from output)
                    if (generatedText != null) {
                        return generatedText.replace(prompt, "").trim();
                    }
                }
            }
            
            throw new RuntimeException("Failed to get valid response from AI API: " + response.getStatusCode());
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating AI analysis: " + e.getMessage());
        }
    }

    private String createAnalysisPrompt(WeatherForecastRequest request) {
        StringBuilder forecastText = new StringBuilder();
        int idx = 1;
        for (WeatherForecastRequest.ForecastDay day : request.getForecast()) {
            forecastText.append(String.format("Day %d (%s): %s, Rainfall: %.1fmm, Temp: %.1f°C - %.1f°C, Rain Chance: %.1f%%, Humidity: %.1f%%\n",
                    idx++, day.getDate(), day.getCondition(), day.getTotalRainfall(), day.getMinTemp(), day.getMaxTemp(), day.getRainChance(), day.getAvgHumidity()));
        }

        return String.format("""
                Analyze the following 7-day weather forecast for %s, %s, Pakistan and provide a flood risk assessment:

                %s

                Provide your analysis in the following JSON format (respond with ONLY valid JSON, no markdown or additional text):

                {
                  "warningLevel": "Low|Moderate|High|Critical",
                  "riskScore": 0-100,
                  "confidence": 0-100,
                  "summary": "Brief 1-2 sentence summary of the flood risk",
                  "keyFactors": ["factor1", "factor2", "factor3"],
                  "recommendations": ["recommendation1", "recommendation2", "recommendation3"],
                  "dailyRisks": [
                    {"date": "YYYY-MM-DD", "risk": "Low|Moderate|High|Critical", "reason": "brief reason"},
                    ...
                  ],
                  "peakRiskDays": ["YYYY-MM-DD", "YYYY-MM-DD"]
                }

                Consider:
                - Total rainfall accumulation over the week
                - Consecutive days of heavy rain
                - Regional flood history in Pakistan
                - Monsoon patterns
                - River basin proximity
                - Soil saturation potential
                """, request.getLocation().getName(), request.getLocation().getRegion(), forecastText.toString());
    }
}
