package ru.reboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.MessageInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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

    @Override
    public MessageInfo getMessage(String messageId) {
        if(Objects.isNull(messageId) || messageId.length()==0){
            throw new BusinessLogicException("MessageId of receiver is not consistent", ErrorCode.ILLEGAL_ARGUMENT);
        }
        MessageEntity entity = messageRepository.getMessage(messageId);
        if(Objects.isNull(entity)){
            throw new BusinessLogicException("No message found",ErrorCode.MESSAGE_NOT_FOUND);
        }

        MessageInfo messageInfo = convertMessageEntityToMessageInfo(entity);

        return messageInfo;
    }

    @Override
    public List<MessageInfo> getAllMessages(String sender, String receiver) {
        if(Objects.isNull(sender) || Objects.isNull(receiver) || sender.length()==0 || receiver.length()==0){
            throw new BusinessLogicException("Sender of receiver is not consistent", ErrorCode.ILLEGAL_ARGUMENT);
        }
        List<MessageEntity> messageEntityList = messageRepository.getAllMessages(sender,receiver);
        if(Objects.isNull(messageEntityList)){
            throw new BusinessLogicException("No messages found",ErrorCode.MESSAGE_NOT_FOUND);
        }
        ArrayList<MessageInfo> messageInfosList = new ArrayList<>();
        for(MessageEntity entity: messageEntityList){
            messageInfosList.add(convertMessageEntityToMessageInfo(entity));
        }
        return messageInfosList;
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
