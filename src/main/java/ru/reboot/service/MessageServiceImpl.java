package ru.reboot.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.MessageInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LogManager.getLogger(MessageServiceImpl.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    private MessageRepository messageRepository;

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Receive message by its messageId
     *
     * @param messageId - Id of message
     * @return - Returns instance of MessageInfo
     */
    @Override
    public MessageInfo getMessage(String messageId) {
        try {
            logger.info("Method .getMessage messageId={}.", messageId);
            if (Objects.isNull(messageId) || messageId.length() == 0) {
                throw new BusinessLogicException("MessageId of receiver is not consistent", ErrorCode.ILLEGAL_ARGUMENT);
            }
            MessageEntity entity = messageRepository.getMessage(messageId);
            if (Objects.isNull(entity)) {
                throw new BusinessLogicException("No message found", ErrorCode.MESSAGE_NOT_FOUND);
            }

            MessageInfo messageInfo = convertMessageEntityToMessageInfo(entity);
            logger.info("Method .getMessage completed  messageId={},result={}", messageId, messageInfo);
            return messageInfo;
        } catch (Exception e) {
            logger.error("Error to .getMessage error = {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Receive all messages with userId
     *
     * @param userId - Chat userId
     * @return - Returns Array of MessageInfo
     */
    @Override
    public List<MessageInfo> getAllMessages(String userId) {
        try {
            logger.info("Method .getAllMessages(String userId) userId={}", userId);
            if (Objects.isNull(userId) || userId.length() == 0) {
                throw new BusinessLogicException("Sender of receiver is not consistent", ErrorCode.ILLEGAL_ARGUMENT);
            }
            List<MessageEntity> messageEntityList = messageRepository.getAllMessages(userId);
            if (Objects.isNull(messageEntityList)) {
                throw new BusinessLogicException("No messages found", ErrorCode.MESSAGE_NOT_FOUND);
            }
            List<MessageInfo> messageInfosList = messageEntityList.stream().map(this::convertMessageEntityToMessageInfo).collect(Collectors.toList());
            logger.info("Method .getAllMessages(String userId) completed userId={},result={}", userId, messageInfosList);
            return messageInfosList;
        } catch (Exception e) {
            logger.error("Error to .getAllMessages(String userId) error = {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Receive all messages between sender and receiver
     *
     * @param sender   - Sender of messages
     * @param receiver - Receiver of messages
     * @return - Returns Array of MessageInfo
     */
    @Override
    public List<MessageInfo> getAllMessages(String sender, String receiver) {
        try {
            logger.info("Method .getAllMessages(String sender, String receiver) sender={},receiver={}", sender, receiver);
            if (Objects.isNull(sender) || Objects.isNull(receiver) || sender.length() == 0 || receiver.length() == 0) {
                throw new BusinessLogicException("Sender of receiver is not consistent", ErrorCode.ILLEGAL_ARGUMENT);
            }
            List<MessageEntity> messageEntityList = messageRepository.getAllMessages(sender, receiver);
            if (Objects.isNull(messageEntityList)) {
                throw new BusinessLogicException("No messages found", ErrorCode.MESSAGE_NOT_FOUND);
            }
            List<MessageInfo> messageInfosList = messageEntityList.stream().map(this::convertMessageEntityToMessageInfo).collect(Collectors.toList());
            logger.info("Method .getAllMessages(String sender, String receiver) completed sender={},receiver={},result={}", sender, receiver, messageInfosList);
            return messageInfosList;
        } catch (Exception e) {
            logger.error("Error to .getAllMessages(String sender, String receiver) error = {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Gets all messages from "message" table
     *
     * @param sender         - sender
     * @param receiver       - receiver
     * @param sinceTimestamp - since time
     * @return List<MessageInfo> or throws exception
     */
    @Override
    public List<MessageInfo> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp) {
        List<MessageInfo> result;
        logger.info("Method .getAllMessages sender={} receiver={} sinceTimestamp={}", sender, receiver, sinceTimestamp);

        try {
            if (sender == null || receiver == null || sinceTimestamp == null
                    || sender.equals("") || receiver.equals("")) {
                throw new BusinessLogicException("Parameters are null or empty", ErrorCode.ILLEGAL_ARGUMENT);
            }
            List<MessageEntity> entityList = messageRepository.getAllMessages(sender, receiver, sinceTimestamp);
            result = entityList.stream().map(this::convertMessageEntityToMessageInfo).collect(Collectors.toList());

            logger.info("Method .getAllMessages completed sender={} receiver={} sinceTimestamp={} result={}", sender, receiver, sinceTimestamp, result);
            return result;
        } catch (Exception e) {
            logger.error("Error to .getAllMessages error = {}", e.getMessage(), e);
            throw e;
        }
    }


    /**
     * Saves message to database
     *
     * @param message - message
     * @return MessageInfo or throws Exception
     */
    @Override
    public MessageInfo saveMessage(MessageInfo message) {
        MessageInfo result;
        logger.info("Method .saveMessage message={} ", message);

        try {
            if (message == null || message.getContent().equals("")) {
                throw new BusinessLogicException("Message is null or empty", ErrorCode.ILLEGAL_ARGUMENT);
            }
            MessageEntity messageEntity = convertMessageInfoToMessageEntity(message);
            result = convertMessageEntityToMessageInfo(messageRepository.saveMessage(messageEntity));

            logger.info("Method .saveMessage completed message={}", message);
            return result;
        } catch (Exception e) {
            logger.error("Ettor to .saveMessage error={}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * @param messages
     * @return
     */
    @Override
    public List<MessageInfo> saveAllMessages(Collection<MessageInfo> messages) {
        logger.info("Method .saveAllMessages messages={}.", messages);
        List<MessageInfo> messageInfos;
        if (messages == null || messages.isEmpty()) {
            throw new BusinessLogicException("Input collection of messages does empty or null", ErrorCode.ILLEGAL_ARGUMENT);
        } else {
            messageInfos = messageRepository.saveAllMessages(messages
                    .stream()
                    .map(this::convertMessageInfoToMessageEntity)
                    .collect(Collectors.toList()))
                    .stream()
                    .map(this::convertMessageEntityToMessageInfo)
                    .collect(Collectors.toList());
            logger.info("Method .saveAllMessages completed messages={} result={}", messages, messages);
            return messageInfos;
        }
    }

    /**
     * @param messageId
     */
    @Override
    public void deleteMessage(String messageId) {
        logger.info("Method .deleteMessage messageId={}.", messageId);
        if (messageId == null || messageId.isEmpty()) {
            throw new BusinessLogicException("messageId is empty or null", ErrorCode.ILLEGAL_ARGUMENT);
        } else {
            Optional
                    .of(convertMessageInfoToMessageEntity(getMessage(messageId)))
                    .orElseThrow(() -> new BusinessLogicException("Message doesn't exist", ErrorCode.MESSAGE_NOT_FOUND));
            messageRepository.deleteMessage(messageId);
            logger.info("Method .deleteMessage completed");
        }
    }

    private MessageInfo convertMessageEntityToMessageInfo(MessageEntity entity) {
        return new MessageInfo.Builder()
                .setId(entity.getId())
                .setSender(entity.getSender())
                .setRecipient(entity.getRecipient())
                .setContent(entity.getContent())
                .setMessageTimestamp(entity.getMessageTimestamp())
                .setLastAccessTime(entity.getLastAccessTime())
                .setReadTime(entity.getReadTime())
                .setWasRead(entity.wasRead())
                .build();
    }

    private MessageEntity convertMessageInfoToMessageEntity(MessageInfo info) {

        String messageId = info.getId();
        if (Objects.isNull(messageId)) {
            messageId = UUID.randomUUID().toString();
        }
        return new MessageEntity.Builder()
                .setId(messageId)
                .setSender(info.getSender())
                .setRecipient(info.getRecipient())
                .setContent(info.getContent())
                .setMessageTimestamp(info.getMessageTimestamp())
                .setLastAccessTime(LocalDateTime.now()) // текущее время!!
                .setReadTime(info.getReadTime())
                .setWasRead(info.getWasRead())
                .build();
    }
}
