package com.legaldocs.eas.ai;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiLlmClient implements LlmClient {
        
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;
    
    @Override
    public String generate(String prompt) {
    	String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + model + ":generateContent?key=" + apiKey;
    	
    	Map<String, Object> body = Map.of(
                "contents", List.of(
                    Map.of(
                        "parts", List.of(
                            Map.of("text", prompt)
                        )
                    )
                )
            );
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);
        
        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        return extractText(response.getBody());
    }
    
    @SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> body) {
    	
    	List<Map<String, Object>> candidates =
                (List<Map<String, Object>>) body.get("candidates");

        if (candidates == null || candidates.isEmpty()) {
            return "No response generated.";
        }
        
        Map<String, Object> content =
                (Map<String, Object>) candidates.get(0).get("content");

            List<Map<String, Object>> parts =
                (List<Map<String, Object>>) content.get("parts");

            return parts.get(0).get("text").toString();
    }
}
