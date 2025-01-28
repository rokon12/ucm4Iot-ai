package ca.tabassum;


import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A library-friendly service for sending messages through LangChain4J.
 * Configurable via {@link LangChain4JConfig}.
 */
@Slf4j
public class LangChainUcm4IoTService {

    private final Ucm4IotAgent ucm4IotAgent;

    public LangChainUcm4IoTService(LangChain4JConfig config) {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(config.getApiKey())
                .modelName(config.getModelName())
                .temperature(config.getTemperature())
                .timeout(config.getTimeout())
                .maxTokens(config.getMaxTokens())
                .frequencyPenalty(config.getFrequencyPenalty())
                .logRequests(config.isLogRequests())
                .logResponses(config.isLogResponses())
                .build();

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .maxMessages(config.getMaxMemorySize())
                .build();

        ucm4IotAgent = AiServices.builder(Ucm4IotAgent.class)
                .chatLanguageModel(chatModel)
                .systemMessageProvider(o -> config.getSystemMessage())
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    /**
     * Sends a user message to the configured OpenAI Chat Model and provides the response
     * to the given {@code consumer}.
     *
     * @param message  The message to send.
     * @param consumer A callback to handle the model’s response.
     */
    public void sendMessage(String message, Consumer<String> consumer) {
        log.info("User message: {}", message);
        String response = ucm4IotAgent.chat(message);
        consumer.accept(response);
    }


    public static void main(String[] args) {
        LangChain4JConfig config = LangChain4JConfig.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .systemMessage("You are an expert in IoT. I am an AI assistant for IoT.")
                .build();

        LangChainUcm4IoTService service = new LangChainUcm4IoTService(config);
        service.sendMessage("What is the weather like today?", System.out::println);
    }
}
