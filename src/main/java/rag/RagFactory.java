package rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import shared.Assistant;

public class RagFactory {

    public static Assistant create(String systemPrompt,
                                   ChatModel chatModel,
                                   EmbeddingModel embeddingModel) {

        EmbeddingStore<TextSegment> historyStore = new InMemoryEmbeddingStore<>();

        ChatHistoryRetriever retriever =
                new ChatHistoryRetriever(historyStore, embeddingModel, 3);

        Assistant baseAssistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .systemMessageProvider(chatMemoryId -> systemPrompt)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .contentRetriever(retriever)
                .build();

        return new ChatHistoryStoreAssistant(baseAssistant, historyStore, embeddingModel);
    }
}
