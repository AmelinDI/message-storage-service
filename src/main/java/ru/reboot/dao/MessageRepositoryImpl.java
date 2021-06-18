package ru.reboot.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Message repository.
 */
@Component
@Transactional
public class MessageRepositoryImpl implements MessageRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
        messages.forEach(this::saveMessage);
        return messages;
    }

    @Override
    public void deleteMessage(String messageId) {
        MessageEntity messageEntity = Optional
                .ofNullable(getMessage(messageId))
                .orElseThrow(() -> new BusinessLogicException("Message doesn't exist", ErrorCode.MESSAGE_NOT_FOUND));
        try (Session session = sessionFactory.openSession()) {
            messageEntity.setMessageTimestamp(LocalDateTime.now());
            session.beginTransaction();
            session.delete(messageEntity);
            session.getTransaction().commit();
        }
    }
}