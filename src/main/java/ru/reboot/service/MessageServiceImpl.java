package ru.reboot.service;

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
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * Receive messages between sender and receiver for all time
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

    @Override
    public Collection<MessageInfo> saveAllMessages(Collection<MessageInfo> messages) {
        return null;
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
                .build();
    }

    private MessageEntity convertMessageInfoToMessageEntity(MessageInfo info) {
        return null;
    }
}
