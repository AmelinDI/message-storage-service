package ru.reboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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

    private MessageRepository messageRepository;

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Reading Set of messages read from Kafka
     *
     * @param raw - serialized CommitMessageEvent instance with Collection of MessageIds
     */
    @KafkaListener(topics = CommitMessageEvent.TOPIC, groupId = "message-storage-service", autoStartup = "${kafka.autoStartup}")
    public void onCommitMessageEvent(String raw) {
        logger.info("Method .onCommitMessageEvent topic={} content={}", CommitMessageEvent.TOPIC, raw);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CommitMessageEvent event = objectMapper.readValue(raw, CommitMessageEvent.class);
            if (event.getMessageIds().isEmpty()) {
                throw new BusinessLogicException("No messagesId", ErrorCode.ILLEGAL_ARGUMENT);
            }
            messageRepository.updateWasReadByIds(event.getMessageIds());
            logger.info("<< Received: {}", raw);
        } catch (Exception e) {
            logger.error("Failed to .onCommitMessageEvent topic={} content={} error={}", CommitMessageEvent.TOPIC, raw, e.toString(), e);
            throw new BusinessLogicException(e.getMessage(), ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    /**
     * Receive message by its messageId
     *
     * @param messageId - Id of message
     * @return - Returns instance of MessageInfo
     */
    @Override
    public MessageInfo getMessage(String messageId) {
        logger.info("Method .getMessage messageId={}", messageId);
        try {
            if (Objects.isNull(messageId) || messageId.length() == 0) {
                throw new BusinessLogicException("MessageId of receiver is not consistent", ErrorCode.ILLEGAL_ARGUMENT);
            }
            MessageEntity entity = messageRepository.getMessage(messageId);
            if (Objects.isNull(entity)) {
                throw new BusinessLogicException("No message found", ErrorCode.MESSAGE_NOT_FOUND);
            }

            MessageInfo messageInfo = convertMessageEntityToMessageInfo(entity);
            logger.info("Method .getMessage completed messageId={} return={}", messageId, messageInfo);
            return messageInfo;
        } catch (Exception e) {
            logger.error("Failed to .getMessage messageId={} error={}", messageId, e.toString(), e);
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
            logger.info("Method .getAllMessages userId={}", userId);
            if (Objects.isNull(userId) || userId.length() == 0) {
                throw new BusinessLogicException("Sender of receiver is not consistent", ErrorCode.ILLEGAL_ARGUMENT);
            }
            List<MessageEntity> messageEntityList = messageRepository.getAllMessages(userId);
            if (Objects.isNull(messageEntityList)) {
                throw new BusinessLogicException("No messages found", ErrorCode.MESSAGE_NOT_FOUND);
            }
            List<MessageInfo> messageInfosList = messageEntityList.stream().map(this::convertMessageEntityToMessageInfo).collect(Collectors.toList());
            logger.info("Method .getAllMessages completed userId={} return={}", userId, messageInfosList);
            return messageInfosList;
        } catch (Exception e) {
            logger.error("Failed to .getAllMessages userId={} error={}", userId, e.toString(), e);
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
        logger.info("Method .getAllMessages sender={} receiver={}", sender, receiver);
        try {
            if (Objects.isNull(sender) || Objects.isNull(receiver) || sender.length() == 0 || receiver.length() == 0) {
                throw new BusinessLogicException("Sender of receiver is not consistent", ErrorCode.ILLEGAL_ARGUMENT);
            }
            List<MessageEntity> messageEntityList = messageRepository.getAllMessages(sender, receiver);
            if (Objects.isNull(messageEntityList)) {
                throw new BusinessLogicException("No messages found", ErrorCode.MESSAGE_NOT_FOUND);
            }
            List<MessageInfo> messageInfosList = messageEntityList.stream().map(this::convertMessageEntityToMessageInfo).collect(Collectors.toList());
            logger.info("Method .getAllMessages completed sender={} receiver={} return={}", sender, receiver, messageInfosList);
            return messageInfosList;
        } catch (Exception e) {
            logger.error("Failed to .getAllMessages sender={} receiver={} error = {}", sender, receiver, e.toString(), e);
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
        logger.info("Method .getAllMessages sender={} receiver={} sinceTimestamp={}", sender, receiver, sinceTimestamp);
        try {
            if (sender == null || receiver == null || sinceTimestamp == null
                    || sender.equals("") || receiver.equals("")) {
                throw new BusinessLogicException("Parameters are null or empty", ErrorCode.ILLEGAL_ARGUMENT);
            }
            List<MessageEntity> entityList = messageRepository.getAllMessages(sender, receiver, sinceTimestamp);
            List<MessageInfo> result = entityList.stream().map(this::convertMessageEntityToMessageInfo).collect(Collectors.toList());

            logger.info("Method .getAllMessages completed sender={} receiver={} sinceTimestamp={} return={}", sender, receiver, sinceTimestamp, result);
            return result;
        } catch (Exception e) {
            logger.error("Failed to .getAllMessages sender={} receiver={} sinceTimestamp={} error={}", sender, receiver, sinceTimestamp, e.toString(), e);
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
        logger.info("Method .saveMessage message={} ", message);
        try {
            if (message == null || message.getContent().equals("")) {
                throw new BusinessLogicException("Message is null or empty", ErrorCode.ILLEGAL_ARGUMENT);
            }
            MessageEntity messageEntity = convertMessageInfoToMessageEntity(message);
            MessageInfo result = convertMessageEntityToMessageInfo(messageRepository.saveMessage(messageEntity));

            logger.info("Method .saveMessage completed message={} return={}", message, result);
            return result;
        } catch (Exception e) {
            logger.error("Failed to .saveMessage message={} error={}", message, e.toString(), e);
            throw e;
        }
    }

    /**
     * Saving messages in DB
     *
     * @param messages - Collection of messages to save in DB
     * @return - List on MessageInfos instances saved in DB
     */
    @Override
    public List<MessageInfo> saveAllMessages(Collection<MessageInfo> messages) {
        logger.info("Method .saveAllMessages messages={}.", messages);
        try{
            if (messages == null || messages.isEmpty()) {
                throw new BusinessLogicException("Input collection of messages does empty or null", ErrorCode.ILLEGAL_ARGUMENT);
            }
            List<MessageInfo> messageInfos = messageRepository.saveAllMessages(messages
                    .stream()
                    .map(this::convertMessageInfoToMessageEntity)
                    .collect(Collectors.toList()))
                    .stream()
                    .map(this::convertMessageEntityToMessageInfo)
                    .collect(Collectors.toList());
            logger.info("Method .saveAllMessages completed messages={} return={}", messages, messages);
            return messageInfos;
        } catch (Exception e) {
            logger.error("Failed to .saveAllMessages messages={} error={}", messages, e.toString(), e);
            throw e;
        }
    }

    /**
     * Delete message from DB by messageId
     *
     * @param messageId - messageId to delete
     */
    @Override
    public void deleteMessage(String messageId) {
        logger.info("Method .deleteMessage messageId={}.", messageId);
        try {
            if (messageId == null || messageId.isEmpty()) {
                throw new BusinessLogicException("messageId is empty or null", ErrorCode.ILLEGAL_ARGUMENT);
            }
            Optional
                    .of(convertMessageInfoToMessageEntity(getMessage(messageId)))
                    .orElseThrow(() -> new BusinessLogicException("Message doesn't exist", ErrorCode.MESSAGE_NOT_FOUND));
            messageRepository.deleteMessage(messageId);

            logger.info("Method .deleteMessage completed messageId={}", messageId);
        } catch (Exception e) {
            logger.error("Failed to .deleteMessage messageId={} error={}", messageId, e.toString(), e);
            throw e;
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
                .setWasRead(entity.getWasRead())
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
