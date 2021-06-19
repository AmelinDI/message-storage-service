package ru.reboot.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.reboot.dao.entity.MessageEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Message repository.
 */
@Component
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
        List<MessageEntity> messageEntities = new ArrayList<>();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            for (MessageEntity message : messages) {
                messageEntities.add(saveMessage(message));
                transaction.commit();
            }
        } catch (RuntimeException e) {
            if (transaction != null)
                transaction.rollback();
        }
        return messageEntities;
    }

    @Override
    public void deleteMessage(String messageId) {
        try (Session session = sessionFactory.openSession()) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setId(messageId);
            session.delete(messageEntity);
        }
    }
}