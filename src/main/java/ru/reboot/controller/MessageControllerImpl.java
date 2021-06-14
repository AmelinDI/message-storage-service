package ru.reboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.reboot.dao.MessageRepositoryImpl;
import ru.reboot.dto.MessageInfo;
import ru.reboot.service.MessageService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Message controller.
 */
@RestController
@RequestMapping(path = "storage")
public class MessageControllerImpl implements MessageController {

    private static final Logger logger = LogManager.getLogger(MessageRepositoryImpl.class);

    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("info")
    public String info() {
        logger.info("method .info invoked");
        return "MessageController " + new Date();
    }

    @Override
    public MessageInfo getMessage(String messageId) {
        return null;
    }

    @Override
    public List<MessageInfo> getAllMessages(String sender, String receiver) {
        return null;
    }

    @Override
    public List<MessageInfo> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp) {
        return null;
    }

    @Override
    public MessageInfo saveMessage(MessageInfo message) {
        return null;
    }

    @Override
    public Collection<MessageInfo> saveAllMessages(Collection<MessageInfo> messages) {
        return null;
    }

    @Override
    public void deleteMessage(String messageId) {

    }
}
