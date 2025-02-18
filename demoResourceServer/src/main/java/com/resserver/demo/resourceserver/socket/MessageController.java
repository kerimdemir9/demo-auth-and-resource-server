package com.resserver.demo.resourceserver.socket;

import com.resserver.demo.resourceserver.data.model.MessageModel;
import com.resserver.demo.resourceserver.data.service.MessageService;
import com.resserver.demo.resourceserver.data.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final MessageService messageService;

    public MessageController(SimpMessagingTemplate messagingTemplate, UserService userService, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.messageService = messageService;
    }

    // ✅ 1️⃣ Broadcast Message (Public)
    @MessageMapping("/broadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public void broadcastMessage(@Payload Message message, Principal principal) {
        messageService.saveMessage(MessageModel.builder()
                .to(null)
                .from(userService.getUser(message.getFrom()))
                .type("BROADCAST")
                .text(message.getText())
                .build());
        
        messagingTemplate.convertAndSend("/topic/public", message);
    }

    // ✅ 2️⃣ Private Message (1-on-1)
    @MessageMapping("/private")
    @PreAuthorize("hasRole('USER')")
    public void privateMessage(@Payload Message message, Principal principal) {
        messageService.saveMessage(MessageModel.builder()
                .to(userService.getUser(message.getTo()))
                .from(userService.getUser(message.getFrom()))
                .type("PRIVATE")
                .text(message.getText())
                .build());
        
        // ✅ Send private message
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/private", message);
        log.info("Message sent to: {}", message.getTo());
    }
}
