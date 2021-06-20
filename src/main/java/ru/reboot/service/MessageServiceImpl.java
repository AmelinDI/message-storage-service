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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private static final Logger logger = LogManager.getLogger(MessageServiceImpl.class);

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Receive message by its messageId
     * @param messageId - Id of message
     * @return - Returns instance of MessageInfo
     */
    @Override
    public MessageInfo getMessage(String messageId) {
        try{
            logger.info("Method .getMessage messageId={}.", messageId);
            if(Objects.isNull(messageId) || messageId.length()==0){
                throw new BusinessLogicException("MessageId of receiver is not consistent", ErrorCode.ILLEGAL_ARGUMENT);
            }
            MessageEntity entity = messageRepository.getMessage(messageId);
            if(Objects.isNull(entity)){
                throw new BusinessLogicException("No message found",ErrorCode.MESSAGE_NOT_FOUND);
            }

            MessageInfo messageInfo = convertMessageEntityToMessageInfo(entity);
            logger.info("Method .getMessage completed  messageId={},result={}", messageId,messageInfo);
            return messageInfo;
        }
        catch (Exception e){
            logger.error("Error to .getMessage error = {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Receive all messages between sender and receiver
     * @param sender - Sender of messages
     * @param receiver - Receiver of messages
     * @return - Returns Array of MessageInfo
     */
    @Override
    public List<MessageInfo> getAllMessages(String sender, String receiver) {
        try{
            logger.info("Method .getAllMessages(String sender, String receiver) sender={},receiver={}", sender,receiver);
            if(Objects.isNull(sender) || Objects.isNull(receiver) || sender.length()==0 || receiver.length()==0){
                throw new BusinessLogicException("Sender of receiver is not consistent", ErrorCode.ILLEGAL_ARGUMENT);
            }
            List<MessageEntity> messageEntityList = messageRepository.getAllMessages(sender,receiver);
            if(Objects.isNull(messageEntityList)){
                throw new BusinessLogicException("No messages found",ErrorCode.MESSAGE_NOT_FOUND);
            }
            List<MessageInfo> messageInfosList = messageEntityList.stream().map(this::convertMessageEntityToMessageInfo).collect(Collectors.toList());
            logger.info("Method .getAllMessages(String sender, String receiver) completed sender={},receiver={},result={}", sender,receiver,messageInfosList);
            return messageInfosList;
        }
        catch (Exception e){
            logger.error("Error to .getAllMessages(String sender, String receiver) error = {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<MessageInfo> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp) {
        return null;
    }

    @Override
    public MessageInfo saveMessage(MessageInfo message) {
        return null;
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

    /**
     * @param messageId
     */
    @Override
    public void deleteMessage(String messageId) {
        logger.info("Method .deleteMessage messageId={}.", messageId);
        if (messageId == null || messageId.isEmpty()) {
            throw new BusinessLogicException("messageId is empty or null", ErrorCode.ILLEGAL_ARGUMENT);
        }
        else {
        Optional
                .of(convertMessageInfoToMessageEntity(getMessage(messageId)))
                .orElseThrow(() -> new BusinessLogicException("Message doesn't exist", ErrorCode.MESSAGE_NOT_FOUND));
        messageRepository.deleteMessage(messageId);
        logger.info("Method .deleteMessage completed");}
    }

    private MessageInfo convertMessageEntityToMessageInfo(MessageEntity entity) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setId(entity.getId());
        messageInfo.setSender(entity.getSender());
        messageInfo.setRecipient(entity.getRecipient());
        messageInfo.setContent(entity.getContent());
        messageInfo.setMessageTimestamp(entity.getMessageTimestamp());
        messageInfo.setLastAccessTime(entity.getLastAccessTime());
        messageInfo.setWasRead(entity.wasRead());
        messageInfo.setReadTime(entity.getReadTime());
        return messageInfo;
    }

    private MessageEntity convertMessageInfoToMessageEntity(MessageInfo info) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(info.getId());
        messageEntity.setSender(info.getSender());
        messageEntity.setRecipient(info.getRecipient());
        messageEntity.setContent(info.getContent());
        messageEntity.setMessageTimestamp(info.getMessageTimestamp());
        messageEntity.setLastAccessTime(LocalDateTime.now());
        messageEntity.setWasRead(info.wasRead());
        messageEntity.setReadTime(info.getReadTime());
        return messageEntity;
    }
}
