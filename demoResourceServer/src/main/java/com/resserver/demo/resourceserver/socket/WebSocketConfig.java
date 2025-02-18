package com.resserver.demo.resourceserver.socket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.messaging.Message;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    final JwtConnectInterceptor jwtConnectInterceptor;
    final JwtSendInterceptor jwtSendInterceptor;

    @Autowired
    public WebSocketConfig(JwtConnectInterceptor jwtConnectInterceptor, JwtSendInterceptor jwtSendInterceptor) {
        this.jwtConnectInterceptor = jwtConnectInterceptor;
        this.jwtSendInterceptor = jwtSendInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") // ✅ WebSocket connection endpoint
                .setAllowedOriginPatterns("*")
                .withSockJS(); // Fallback for older browsers
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic", "/user"); // ✅ Enable in-memory message broker
        registry.setApplicationDestinationPrefixes("/app"); // ✅ Prefix for app messages
        registry.setUserDestinationPrefix("/user"); // ✅ Prefix for user messages
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtConnectInterceptor, jwtSendInterceptor); // Attach JWT authentication
    }
}
