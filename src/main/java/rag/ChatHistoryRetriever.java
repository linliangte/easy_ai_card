package rag;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;

import java.util.List;
import java.util.stream.Collectors;

public class ChatHistoryRetriever implements ContentRetriever {

    private final EmbeddingStore<TextSegment> store;
    private final EmbeddingModel embeddingModel;
    private final int maxResults;

    public ChatHistoryRetriever(EmbeddingStore<TextSegment> store,
                                EmbeddingModel embeddingModel,
                                int maxResults) {
        this.store = store;
        this.embeddingModel = embeddingModel;
        this.maxResults = maxResults;
    }

    @Override
    public List<Content> retrieve(Query query) {
        Embedding queryEmbedding = embeddingModel.embed(query.text()).content();

        List<EmbeddingMatch<TextSegment>> matches = store.search(
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(maxResults)
                        .minScore(0.70)
                        .build()
        ).matches();

        return matches.stream()
                .map(match -> Content.from("[过去的对话]\n" + match.embedded().text()))
                .collect(Collectors.toList());
    }
}