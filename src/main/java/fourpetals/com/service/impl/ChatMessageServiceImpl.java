package fourpetals.com.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fourpetals.com.dto.request.ChatMessageRequest;
import fourpetals.com.dto.response.ChatMessageResponse;
import fourpetals.com.entity.ChatMessage;
import fourpetals.com.mapper.ChatMessageMapper;
import fourpetals.com.repository.ChatMessageRepository;
import fourpetals.com.repository.UserRepository;
import fourpetals.com.service.ChatMessageService;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

	private final ChatMessageRepository chatRepo;
	private final UserRepository userRepo;

	public ChatMessageServiceImpl(ChatMessageRepository chatRepo, UserRepository userRepo) {
		super();
		this.chatRepo = chatRepo;
		this.userRepo = userRepo;
	}

	@Override
	public ChatMessageResponse sendMessage(ChatMessageRequest request) {
		ChatMessage msg = ChatMessageMapper.toEntity(request);

		if (request.isNoiBo()) {
			// Map User cho nhân viên
			if (request.getNguoiGuiId() != null)
				userRepo.findById(request.getNguoiGuiId()).ifPresent(msg::setNguoiGui);
			if (request.getNguoiNhanId() != null)
				userRepo.findById(request.getNguoiNhanId()).ifPresent(msg::setNguoiNhan);
			if (request.getNhanVienPhuTrachId() != null)
				userRepo.findById(request.getNhanVienPhuTrachId()).ifPresent(msg::setNhanVienPhuTrach);
		}

		ChatMessage saved = chatRepo.save(msg);
		return ChatMessageMapper.toResponse(saved);
	}

	@Override
	public List<ChatMessageResponse> getAllMessages() {
		return chatRepo.findAll().stream().map(ChatMessageMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ChatMessageResponse> getMessagesByUser(Integer userId) {
		List<ChatMessage> list = chatRepo.findByNguoiGuiUserIdOrNguoiNhanUserId(userId, userId);
		return list.stream().map(ChatMessageMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ChatMessageResponse> getMessagesByType(boolean isNoiBo) {
		List<ChatMessage> list = chatRepo.findByIsNoiBo(isNoiBo);
		return list.stream().map(ChatMessageMapper::toResponse).collect(Collectors.toList());
	}
}