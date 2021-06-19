package ru.reboot.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.MessageInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private static final Logger logger = LogManager.getLogger(MessageServiceImpl.class);

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public MessageInfo getMessage(String messageId) {
        return null;
    }

    @Override
    public List<MessageInfo> getAllMessages(String sender, String receiver) {
        return null;
    }

    /**
     * Gets all messages from "message" table
     *
     * @param sender - sender
     * @param receiver - receiver
     * @param sinceTimestamp - since time
     * @return List<MessageInfo> or throws exception
     */
    @Override
    public List<MessageInfo> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp) {
        List<MessageInfo> result;
        logger.info("Method .getAllMessages sender={} receiver={} sinceTimestamp={}", sender, receiver, sinceTimestamp);

        try{
            if (sender == null || receiver == null || sinceTimestamp == null
                    || sender.equals("") || receiver.equals("")) {
                throw new BusinessLogicException("Parameters are null or empty", ErrorCode.ILLEGAL_ARGUMENT);
            }
            List<MessageEntity> entityList = messageRepository.getAllMessages(sender, receiver, sinceTimestamp);
            result = entityList.stream().map(this::convertMessageEntityToMessageInfo).collect(Collectors.toList());

            logger.info("Method .getAllMessages completed sender={} receiver={} sinceTimestamp={} result={}", sender, receiver, sinceTimestamp, result);
            return result;
        } catch (Exception e){
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
                throw new BusinessLogicException("Message is null or empty",ErrorCode.ILLEGAL_ARGUMENT);
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
    public Collection<MessageInfo> saveAllMessages(Collection<MessageInfo> messages) {
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

    @Override
    public void deleteMessage(String messageId) {

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
        return new MessageEntity.Builder()
                .setId(info.getId())
                .setSender(info.getSender())
                .setRecipient(info.getRecipient())
                .setContent(info.getContent())
                .setMessageTimestamp(info.getMessageTimestamp())
                .setLastAccessTime(LocalDateTime.now()) // текущее время!!
                .setReadTime(info.getReadTime())
                .setWasRead(info.wasRead())
                .build();
    }
}
