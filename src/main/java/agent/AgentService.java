package agent;

import llm.LlmService;
import rag.RagFactory;
import shared.Assistant;

public class AgentService {

    private final WorldAgentRouter router;
    private final Assistant ragAssistant;
    private final LlmService llmService;

    public AgentService(WorldAgentRouter router,
                        Assistant ragAssistant,
                        LlmService llmService) {

        this.router = router;
        this.ragAssistant = ragAssistant;
        this.llmService = llmService;
    }

    public String answer(String query) {

        WorldAgentRouter.Route route = router.route(query);

        if (route == WorldAgentRouter.Route.RAG) {
            return ragAssistant.answer(query);
        }

        return llmService.chat(query);
    }
}