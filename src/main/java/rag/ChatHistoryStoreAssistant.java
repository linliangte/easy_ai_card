package rag;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import shared.Assistant;

public class ChatHistoryStoreAssistant implements Assistant {

    private final Assistant delegate;
    private final EmbeddingStore<TextSegment> historyStore;
    private final EmbeddingModel embeddingModel;

    public ChatHistoryStoreAssistant(Assistant delegate,
                                     EmbeddingStore<TextSegment> historyStore,
                                     EmbeddingModel embeddingModel) {
        this.delegate = delegate;
        this.historyStore = historyStore;
        this.embeddingModel = embeddingModel;
    }

    @Override
    public String answer(String query) {
        String response = delegate.answer(query);

        TextSegment segment = TextSegment.from(
                "User: " + query + "\nAssistant: " + response);
        Embedding embedding = embeddingModel.embed(segment.text()).content();
        historyStore.add(embedding, segment);

        return response;
    }
}
