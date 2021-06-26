package ru.reboot.dao;

import ru.reboot.dao.entity.MessageEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MessageRepository {

    /**
     * Get message by id
     */
    MessageEntity getMessage(String messageId);

    /**
     * Get all messages with user
     */
    List<MessageEntity> getAllMessages(String userId);

    /**
     * Get all messages between sender and receiver
     */
    List<MessageEntity> getAllMessages(String sender, String receiver);

    /**
     * Get all messages between sender and receiver since timestamp
     */
    List<MessageEntity> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp);

    /**
     * Save message
     */
    MessageEntity saveMessage(MessageEntity message);

    /**
     * Save all messages
     */
    Collection<MessageEntity> saveAllMessages(Collection<MessageEntity> messages);

    /**
     * Delete message by id
     */
    void deleteMessage(String messageId);
}
