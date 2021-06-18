package ru.reboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.MessageInfo;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

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

    @Override
    public List<MessageInfo> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp) {
        List<MessageInfo> result = new ArrayList<>();
        List<MessageEntity> entityList = messageRepository.getAllMessages(sender, receiver, sinceTimestamp);

        return entityList.stream().map(this::convertMessageEntityToMessageInfo).collect(Collectors.toList());
    }

    @Override
    public MessageInfo saveMessage(MessageInfo message) {
        MessageEntity messageEntity = convertMessageInfoToMessageEntity(message);
        return convertMessageEntityToMessageInfo(messageRepository.saveMessage(messageEntity));
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
                .setLastAccessTime((entity.getLastAccessTime()))
                .build();
    }

    private MessageEntity convertMessageInfoToMessageEntity(MessageInfo info) {
        return new MessageEntity.Builder()
                .setId(info.getId())
                .setSender(info.getSender())
                .setRecipient(info.getRecipient())
                .setContent(info.getContent())
                .setMessageTimestamp(info.getMessageTimestamp())
                .setLastAccessTime(info.getLastAccessTime())
                .build();
    }
}
