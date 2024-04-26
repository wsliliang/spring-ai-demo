package com.ll.springai.controller;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SimpleAiController {

    @Autowired
    private ChatClient openAiChatClient;

    @GetMapping("/ai/simple")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", openAiChatClient.call(message));
    }

    @GetMapping("/ai/simple/template")
    public Map<String, String> completionWithTemplate(@RequestParam(value = "topic", defaultValue = "编程") String topic) {
        PromptTemplate promptTemplate = new PromptTemplate("给我推荐一本关于{topic}的书");
        Prompt prompt = promptTemplate.create(Map.of("topic", topic));
        return Map.of("generation", openAiChatClient.call(prompt).getResult().getOutput().getContent());
    }
}
