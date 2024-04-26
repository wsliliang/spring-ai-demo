package com.ll.springai.controller;

import com.ll.springai.service.RagService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RagController {
	private final static Logger log = LoggerFactory.getLogger(RagController.class);

	@Autowired
	private RagService ragService;
	@Autowired
	private VectorStore chromaVectorStore;

	@Value("classpath:/data/docker-network.txt") // This is the text document to load
	private Resource dockerResource;

	@Value("classpath:/data/spring.txt") // This is the text document to load
	private Resource springResource;

	@PostConstruct
	public void init() {
		// docker文档
		/*TextReader textReader = new TextReader(dockerResource);
		textReader.getCustomMetadata().put("filename", "docker-network.txt");
		TextSplitter tokenTextSplitter = new TokenTextSplitter(120, 10, 5, 1000, true);
		chromaVectorStore.accept(tokenTextSplitter.apply(textReader.get()));

		// spring文档
		TextReader springReader = new TextReader(springResource);
		springReader.getCustomMetadata().put("filename", "spring.txt");
		chromaVectorStore.accept(tokenTextSplitter.apply(springReader.get()));*/

		log.info("chroma数据库初始化完成.");
	}

	@GetMapping("/ai/queryChroma")
	public Map<String, Object> embedDoc(@RequestParam(value = "query", defaultValue = "怎样创建虚拟网桥?") String query) {
		List<Document> documents = chromaVectorStore.similaritySearch(query);
		log.info("从chroma中提取数据,问题:{},结果:{}", query, documents);
		return Map.of("query",query,"documents",documents);
	}

	@GetMapping("/ai/rag")
	public AssistantMessage queryDoc(@RequestParam(value = "query", defaultValue = "怎样创建虚拟网桥?") String query) {
		return ragService.retriveFromChromaAndAnswer(query);
	}

}