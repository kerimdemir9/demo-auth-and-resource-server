package com.resserver.demo.resourceserver.data.service;

import com.resserver.demo.resourceserver.data.model.MessageModel;
import com.resserver.demo.resourceserver.data.model.UserModel;
import com.resserver.demo.resourceserver.data.repository.MessageRepository;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageModel saveMessage(MessageModel message) {
        try {
            message.setSeen(false);
            message.setCreated(new Date(Instant.now().toEpochMilli()));
            return messageRepository.save(message);
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Message save failed.", e);
        }
    }
    
    public List<MessageModel> getMessagesFrom(UserModel user) {
        try {
            val found = messageRepository.findAllByFrom(user);
            if(found.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Messages not found.");
            }
            return found;
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public List<MessageModel> getMessagesTo(UserModel user) {
        try {
            val found = messageRepository.findAllByTo(user);
            if(found.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Messages not found.");
            }
            return found;
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }
}

