package com.resserver.demo.resourceserver.socket;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class JwtSendInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;

    @Autowired
    public JwtSendInterceptor(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SEND.equals(accessor.getCommand())) {
            // ✅ Authenticate WebSocket connection
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token == null || !token.startsWith("Bearer ")) {
                throw new AccessDeniedException("Authorization header missing or invalid");
            }

            token = token.substring(7); // Remove "Bearer " prefix

            try {
                Jwt jwt = jwtDecoder.decode(token);
                String username = jwt.getClaimAsString("sub");

                String connectedUser = (String) accessor.getSessionAttributes().get("connectedUser");
                if (connectedUser == null || !connectedUser.equals(username)) {
                    throw new AccessDeniedException("User identity mismatch. Possible token forgery.");
                }

                List<String> roles = jwt.getClaimAsStringList("authorities");

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                accessor.setUser(authentication);

                // ✅ Set SecurityContext for message processing (ensures @PreAuthorize works)
                SecurityContextHolder.getContext().setAuthentication(authentication);

                
                
                log.info("WebSocket message authenticated for user: {}", username);
            } catch (JwtException e) {
                log.warn("WebSocket connection rejected: Invalid JWT Token.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or Expired JWT Token", e);
            }
        }

        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        SecurityContextHolder.clearContext();
        log.info("Cleared SecurityContext after message processing.");
    }
}




