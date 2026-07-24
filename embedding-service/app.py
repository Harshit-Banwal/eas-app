from fastapi import FastAPI
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer

app = FastAPI()

model = SentenceTransformer("sentence-transformers/all-MiniLM-L6-v2")

class TextRequest(BaseModel):
    text: str


@app.get("/")
def root():
    return "FAST API"

@app.post("/embed")
def embed(req: TextRequest):
    embedding = model.encode(req.text, normalize_embeddings=True)

    return {
        "embedding": embedding.tolist()
    }