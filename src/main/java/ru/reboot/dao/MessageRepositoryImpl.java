package ru.reboot.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.reboot.dao.entity.MessageEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        return messages.stream().map(message -> {
            Transaction transaction = null;
            MessageEntity messageEntity = null;
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                messageEntity = saveMessage(message);
                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction != null)
                    transaction.rollback();
            }
            return messageEntity;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteMessage(String messageId) {
        try (Session session = sessionFactory.openSession()) {
            MessageEntity messageEntity = new MessageEntity();
            session.delete(messageEntity);
        }
    }
}