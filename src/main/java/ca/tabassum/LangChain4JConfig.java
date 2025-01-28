package ca.tabassum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LangChain4JConfig {

    @Builder.Default
    private String apiKey = "";

    @Builder.Default
    private String modelName = "gpt-3.5-turbo";

    @Builder.Default
    private double temperature = 0.7;

    @Builder.Default
    private Duration timeout = Duration.ofSeconds(120);

    @Builder.Default
    private int maxTokens = 2048;

    @Builder.Default
    private double frequencyPenalty = 0.0;

    @Builder.Default
    private boolean logRequests = false;

    @Builder.Default
    private boolean logResponses = false;

    @Builder.Default
    private String systemMessage = "";

    @Builder.Default
    private int maxMemorySize = 20;
}
