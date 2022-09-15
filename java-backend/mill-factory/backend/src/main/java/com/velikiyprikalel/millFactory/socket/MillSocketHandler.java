package com.velikiyprikalel.millFactory.socket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velikiyprikalel.millFactory.MillService;

public class MillSocketHandler extends TextWebSocketHandler {
	private static final ObjectMapper mapper = new ObjectMapper(); 

	private final MillService service;

	public MillSocketHandler(MillService service) {
		this.service = service;
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		TextMessage textMessage = new TextMessage(mapper.writeValueAsString(service.getState()));
		session.sendMessage(textMessage);
	}
}
