package com.legaldocs.eas.extraction;

import java.nio.ByteBuffer;

import org.springframework.stereotype.Service;

@Service
public class EmbeddingService {
	
	// STUB for now – replace with OpenAI / Gemini / local model
    public float[] embed(String text) {

        // Temporary deterministic embedding (for testing RAG flow)
        float[] vector = new float[384];
        int seed = Math.abs(text.hashCode());

        for (int i = 0; i < vector.length; i++) {
            vector[i] = ((seed + i) % 1000) / 1000f;
        }
        return vector;
    }
    
    
}
