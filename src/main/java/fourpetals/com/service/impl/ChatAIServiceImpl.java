package fourpetals.com.service.impl;

import org.springframework.stereotype.Service;

import fourpetals.com.dto.chat.ChatAIRequest;
import fourpetals.com.service.ChatAIService;

@Service
public class ChatAIServiceImpl implements ChatAIService{

	@Override
	public String askAI(ChatAIRequest request) {
		return request.message();
	}

}
