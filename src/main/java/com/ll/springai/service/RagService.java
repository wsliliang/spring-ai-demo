package com.ll.springai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RagService {

	private static final Logger logger = LoggerFactory.getLogger(RagService.class);

	@Value("classpath:/prompts/system-qa.st")
	private Resource systemBikePrompt;

	@Autowired
	private ChatClient openAiChatClient;

	@Autowired
	private VectorStore chromaVectorStore;

	public AssistantMessage retriveFromChromaAndAnswer(String message) {
		List<Document> similarDocuments = chromaVectorStore.similaritySearch(message);
		logger.info(String.format("Found %s relevant documents.", similarDocuments.size()));

		Message systemMessage = getSystemMessage(similarDocuments);
		UserMessage userMessage = new UserMessage(message);

		logger.info("Asking AI model to reply to question.");
		Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
		logger.info(prompt.toString());

		ChatResponse chatResponse = openAiChatClient.call(prompt);
		logger.info("AI responded.");

		logger.info(chatResponse.getResult().getOutput().getContent());
		return chatResponse.getResult().getOutput();
	}

	private Message getSystemMessage(List<Document> similarDocuments) {

		String documents = similarDocuments.stream().map(entry -> entry.getContent()).collect(Collectors.joining("\n"));
		SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemBikePrompt);
		Message systemMessage = systemPromptTemplate.createMessage(Map.of("documents", documents));
		return systemMessage;

	}

}