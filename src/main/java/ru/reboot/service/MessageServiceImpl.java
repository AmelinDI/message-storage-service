package ru.reboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.MessageInfo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Component
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public MessageEntity getMessage(String messageId) {
        return null;
    }

    @Override
    public List<MessageEntity> getAllMessages(String sender, String receiver) {
        return null;
    }

    @Override
    public List<MessageEntity> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp) {
        return null;
    }

    @Override
    public MessageEntity saveMessage(MessageEntity message) {
        return null;
    }

    @Override
    public Collection<MessageEntity> saveAllMessages(Collection<MessageEntity> messages) {
        return null;
    }

    @Override
    public void deleteMessage(String messageId) {

    }
}
