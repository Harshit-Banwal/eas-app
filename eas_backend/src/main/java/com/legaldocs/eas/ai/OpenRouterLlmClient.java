package com.legaldocs.eas.ai;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Primary
public class OpenRouterLlmClient implements LlmClient {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Value("${openrouter.api-key}")
    private String apiKey;

    @Value("${openrouter.model}")
    private String model;
	
	@Override
    public String generate(String prompt) {

        String url = "https://openrouter.ai/api/v1/chat/completions";

        Map<String, Object> body = Map.of(
            "model", model,
            "messages", List.of(
            	    Map.of(
            	        "role", "system",
            	        "content", "You are a precise legal AI that returns structured JSON only."
            	    ),
            	    Map.of(
            	        "role", "user",
            	        "content", prompt
            	    )
            	),
            "temperature", 0
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        return extractText(response.getBody());
    }
	
	@SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> body) {

        List<Map<String, Object>> choices =
            (List<Map<String, Object>>) body.get("choices");

        if (choices == null || choices.isEmpty()) {
            return "No response generated.";
        }

        Map<String, Object> message =
            (Map<String, Object>) choices.get(0).get("message");

        return message.get("content").toString();
    }
}
