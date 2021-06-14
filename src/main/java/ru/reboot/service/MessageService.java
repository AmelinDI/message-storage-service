package ru.reboot.service;

import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.MessageInfo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MessageService {

    /**
     * Get message by id
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if message id is null or empty
     * @throws ru.reboot.error.BusinessLogicException with code MESSAGE_NOT_FOUND if message not found
     */
    MessageEntity getMessage(String messageId);

    /**
     * Get all messages between sender and receiver
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if sender, receiver is null or empty
     */
    List<MessageEntity> getAllMessages(String sender, String receiver);

    /**
     * Get all messages between sender and receiver since timestamp
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if sender, receiver is null or empty
     */
    List<MessageEntity> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp);

    /**
     * Save message
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if message is null
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if message has bad data
     */
    MessageEntity saveMessage(MessageEntity message);

    /**
     * Save all messages
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if messages is null
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if messages has bad data
     */
    Collection<MessageEntity> saveAllMessages(Collection<MessageEntity> messages);

    /**
     * Delete message by id
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if message id is null or empty
     * @throws ru.reboot.error.BusinessLogicException with code MESSAGE_NOT_FOUND if message not found
     */
    void deleteMessage(String messageId);
}
