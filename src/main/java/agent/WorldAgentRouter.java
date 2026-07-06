package agent;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;

public class WorldAgentRouter {

    private final EmbeddingModel embeddingModel;
    private final Embedding anchor;

    public WorldAgentRouter(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
        this.anchor = embeddingModel.embed("世界设定 角色 背景 规则").content();
    }

    public Route route(String query) {
        Embedding q = embeddingModel.embed(query).content();
        double score = cosine(q, anchor);
        return score > 0.70 ? Route.RAG : Route.LLM;
    }

    private double cosine(Embedding a, Embedding b) {
        float[] x = a.vector();
        float[] y = b.vector();
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < x.length; i++) {
            dot += x[i] * y[i];
            na += x[i] * x[i];
            nb += y[i] * y[i];
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    public enum Route {
        RAG, LLM
    }
}