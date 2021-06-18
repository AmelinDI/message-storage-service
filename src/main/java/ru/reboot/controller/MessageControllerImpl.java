package ru.reboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("message/allSinceTime")
    public List<MessageInfo> getAllMessages(@RequestParam("sender") String sender,@RequestParam("receiver") String receiver,@RequestParam("timestamp") LocalDateTime sinceTimestamp) {
        messageService.getAllMessages(sender, receiver, sinceTimestamp);
        return null;
    }

    @Override
    @PutMapping("/message")
    public MessageInfo saveMessage(@RequestBody MessageInfo message) {
        messageService.saveMessage(message);
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
