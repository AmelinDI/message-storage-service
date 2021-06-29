package ru.reboot.service;

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
    MessageInfo getMessage(String messageId);

    /**
     * Get all messages with user
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if user is null or empty
     */
    List<MessageInfo> getAllMessages(String user);

    /**
     * Get all messages between sender and receiver
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if sender, receiver is null or empty
     */
    List<MessageInfo> getAllMessages(String sender, String receiver);

    /**
     * Get all messages between sender and receiver since timestamp
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if sender, receiver is null or empty
     */
    List<MessageInfo> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp);

    /**
     * Save message
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if message is null
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if message has bad data
     */
    MessageInfo saveMessage(MessageInfo message);

    /**
     * Save all messages
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if messages is null
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if messages has bad data
     */
    Collection<MessageInfo> saveAllMessages(Collection<MessageInfo> messages);

    /**
     * Delete message by id
     *
     * @throws ru.reboot.error.BusinessLogicException with code ILLEGAL_ARGUMENT if message id is null or empty
     * @throws ru.reboot.error.BusinessLogicException with code MESSAGE_NOT_FOUND if message not found
     */
    void deleteMessage(String messageId);
}
