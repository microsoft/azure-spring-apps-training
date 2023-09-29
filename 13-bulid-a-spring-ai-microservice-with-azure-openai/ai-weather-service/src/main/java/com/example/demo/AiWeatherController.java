package com.example.demo;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.Generation;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/weather")
public class AiWeatherController {

    private final AiClient aiClient;

    @Value("classpath:/prompts/weather-service.st")
    private Resource weatherServiceTemplate;

    @Autowired
    public AiWeatherController(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    @PostMapping("/ask")
    public List<Generation> askAi(@RequestParam String question) {
        PromptTemplate systemMessageTemplate = new SystemPromptTemplate(weatherServiceTemplate);
        String weatherHistory = WeatherData.BASEL;
        Message systemMessage = systemMessageTemplate.createMessage(Map.of(
                "today", LocalDate.now().toString(),
                "city", "Basel",
                "weatherHistory", weatherHistory));
        Prompt prompt = new Prompt(List.of(systemMessage, new UserMessage(question)));
        List<Generation> response = aiClient.generate(prompt).getGenerations();
        return response;
    }
}
