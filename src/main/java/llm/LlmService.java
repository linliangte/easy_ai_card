package llm;

import dev.langchain4j.model.chat.ChatModel;

public class LlmService {

    private final ChatModel chatModel;

    public LlmService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String chat(String query) {

        return chatModel.chat(
                "你是一个智能助手，直接回答问题，不依赖知识库。\n用户问题：" + query
        );
    }
}