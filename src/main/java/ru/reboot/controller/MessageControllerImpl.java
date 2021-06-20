package ru.reboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    private static final Logger logger = LogManager.getLogger(MessageControllerImpl.class);

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

    @GetMapping("/message")
    @Override
    public MessageInfo getMessage(@RequestParam String messageId) {
        return messageService.getMessage(messageId);
    }

    @GetMapping("/message/all")
    @Override
    public List<MessageInfo> getAllMessages(@RequestParam("sender") String sender,@RequestParam("receiver") String receiver) {
        return messageService.getAllMessages(sender,receiver);
    }

    @Override
    @GetMapping("/message/allSinceTime")
    public List<MessageInfo> getAllMessages(@RequestParam("sender") String sender,@RequestParam("receiver") String receiver,@RequestParam("timestamp") String stringTimestamp) {
        LocalDateTime sinceTimestamp = LocalDateTime.parse(stringTimestamp);
        List<MessageInfo> result = messageService.getAllMessages(sender, receiver, sinceTimestamp);
        return result;
    }

    @Override
    @PutMapping("/message")
    public MessageInfo saveMessage(@RequestBody MessageInfo message) {
        return messageService.saveMessage(message);
    }

    @Override
    @PutMapping("/message/all")
    public Collection<MessageInfo> saveAllMessages(Collection<MessageInfo> messages) {
        return messageService.saveAllMessages(messages);
    }

    @Override
    @DeleteMapping("/message/{messageId}")
    public void deleteMessage(@PathVariable String messageId) {
        messageService.deleteMessage(messageId);
    }
}