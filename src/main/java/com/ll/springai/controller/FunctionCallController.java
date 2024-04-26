package com.ll.springai.controller;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class FunctionCallController {

	@Autowired
	private ChatClient openAiChatClient;

	@GetMapping("/ai/weather")
	public Map<String, String> functionCall(@RequestParam(value = "message", defaultValue = "北京天气怎么样?") String message) {
		UserMessage userMessage = new UserMessage(message);

		ChatResponse response = openAiChatClient.call(
				new Prompt(List.of(userMessage), OpenAiChatOptions.builder().withFunction("WeatherInfo").build()));

		return Map.of("generation", response.getResult().getOutput().getContent());
	}

}
