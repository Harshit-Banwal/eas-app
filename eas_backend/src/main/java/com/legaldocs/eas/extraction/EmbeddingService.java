package com.legaldocs.eas.extraction;

import java.nio.ByteBuffer;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.legaldocs.eas.auth.dto.EmbeddingRequest;
import com.legaldocs.eas.auth.dto.EmbeddingResponse;

@Service
public class EmbeddingService {
	
	private final RestClient restClient;

    public EmbeddingService(RestClient restClient) {
        this.restClient = restClient;
    }
	
    public float[] embed(String text) {

    	EmbeddingResponse response = restClient.post()
                .uri("http://localhost:8000/embed")
                .body(new EmbeddingRequest(text))
                .retrieve()
                .body(EmbeddingResponse.class);

        if (response == null || response.getEmbedding() == null) {
            throw new RuntimeException("Embedding service returned no data");
        }

        List<Float> list = response.getEmbedding();

        float[] vector = new float[list.size()];

        for (int i = 0; i < list.size(); i++) {
            vector[i] = list.get(i);
        }

        return vector;
    }
    
    
}
