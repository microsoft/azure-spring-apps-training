package com.example.demo;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.Generation;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/weather")
public class AiWeatherController {

    public static final String SYSTEM_MESSAGE = "You are an AI assistant that helps forecast weather. Don't use any external data.";
    private final AiClient aiClient;

    @Value("classpath:/prompts/weather-forecast.st")
    private Resource weatherTemplate;

    @Autowired
    public AiWeatherController(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    @GetMapping("/city")
    public List<Generation> forecastByAi(@RequestParam("name") String city) {
        Message systemMessage = new SystemMessage(SYSTEM_MESSAGE);
        PromptTemplate userMessageTemplate = new PromptTemplate(weatherTemplate);
        String weatherHistory = switch (city.toLowerCase()) {
            case "basel" -> WeatherData.BASEL;
            case "belgium" -> WeatherData.BELGIUM;
            default -> "";
        };
        Message userMessage = userMessageTemplate.createMessage(Map.of(
                "today", LocalDate.now().toString(),
                "city", city,
                "weatherHistory", weatherHistory));
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        List<Generation> response = aiClient.generate(prompt).getGenerations();
        return response;
    }
}
