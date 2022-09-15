package com.velikiyprikalel.millFactory.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.velikiyprikalel.millFactory.MillService;

@Configuration
@EnableWebSocket
public class SocketConfiguration implements WebSocketConfigurer {

	private final MillService service;

	public SocketConfiguration(MillService service) {
		this.service = service;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new MillSocketHandler(service), "/websocket").setAllowedOrigins("*");
		
	}
	
}
