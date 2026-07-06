package _1_easy;

import agent.AgentService;
import agent.WorldAgentRouter;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.model.embedding.EmbeddingModel;

import llm.LlmService;
import rag.RagFactory;
import shared.Assistant;
import shared.Utils;

import java.util.List;
import java.util.Scanner;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import static shared.Utils.*;

public class Easy_RAG_Example {

    // ===== LLM（大模型） =====
    private static final ChatModel CHAT_MODEL = OpenAiChatModel.builder()
            .apiKey(OPENAI_API_KEY)
            .baseUrl("https://ws-01xqqx90gae93m51.cn-beijing.maas.aliyuncs.com/compatible-mode/v1")
            .modelName("qwen3.6-flash")
            .build();

    // ===== embedding（本地 Ollama）=====
    private static final EmbeddingModel EMBEDDING_MODEL =
            OllamaEmbeddingModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName("nomic-embed-text")
                    .build();

    public static void main(String[] args) {

        List<Document> docs =
                loadDocuments(Utils.toPath("world/"), Utils.glob("*.md"));

        Assistant rag = RagFactory.create(docs, CHAT_MODEL, EMBEDDING_MODEL);

        WorldAgentRouter router = new WorldAgentRouter(EMBEDDING_MODEL);

        LlmService llm = new LlmService(CHAT_MODEL);

        AgentService agent = new AgentService(router, rag, llm);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("你：");
            String q = sc.nextLine();

            System.out.println("AI：" + agent.answer(q));
        }
    }
}