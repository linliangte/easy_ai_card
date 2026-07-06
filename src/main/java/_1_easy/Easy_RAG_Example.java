package _1_easy;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import rag.RagFactory;
import shared.Assistant;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

import static shared.Utils.OPENAI_API_KEY;

public class Easy_RAG_Example {

    private static final ChatModel CHAT_MODEL = OpenAiChatModel.builder()
            .apiKey(OPENAI_API_KEY)
            .baseUrl("https://ws-01xqqx90gae93m51.cn-beijing.maas.aliyuncs.com/compatible-mode/v1")
            .modelName("qwen3.6-flash")
            .build();

    private static final EmbeddingModel EMBEDDING_MODEL =
            OllamaEmbeddingModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName("nomic-embed-text")
                    .build();

    public static void main(String[] args) throws IOException {

        String systemPrompt = loadWorldFiles();

        Assistant assistant = RagFactory.create(systemPrompt, CHAT_MODEL, EMBEDDING_MODEL);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("你");
            String q = sc.nextLine();
            System.out.println("AI" + assistant.answer(q));
        }
    }

    private static String loadWorldFiles() throws IOException {
        Path worldDir = Path.of("src/main/resources/world");
        StringBuilder sb = new StringBuilder();
        try (Stream<Path> stream = Files.list(worldDir)) {
            stream.filter(p -> p.toString().endsWith(".md"))
                    .sorted()
                    .forEach(p -> {
                        try {
                            sb.append(Files.readString(p, StandardCharsets.UTF_8)).append("\n\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return sb.toString().trim();
    }
}