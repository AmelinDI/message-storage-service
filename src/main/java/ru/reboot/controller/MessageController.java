package ru.reboot.controller;

import ru.reboot.dto.MessageInfo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MessageController {

    /**
     * Get message by id
     */
    MessageInfo getMessage(String messageId);

    /**
     * Get all messages between sender and receiver
     */
    List<MessageInfo> getAllMessages(String sender, String receiver);

    /**
     * Get all messages between sender and receiver since timestamp
     * @return
     */
    List<MessageInfo> getAllMessages(String sender,String receiver,String sinceTimestamp);

    /**
     * Save message
     */
    MessageInfo saveMessage(MessageInfo message);

    /**
     * Save all messages
     */
    Collection<MessageInfo> saveAllMessages(Collection<MessageInfo> messages);

    /**
     * Delete message by id
     */
    void deleteMessage(String messageId);
}
