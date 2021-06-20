package ru.reboot.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
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
        MessageEntity entity;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from MessageEntity m where m.id=:id");
            query.setParameter("id", messageId);
            entity = (MessageEntity) query.getSingleResult();
        }
        catch (NoResultException exp){
            entity = null;
        }
        catch (RuntimeException exp) {
            throw new BusinessLogicException("Exception in DB: " + exp.getMessage(), ErrorCode.DATABASE_ERROR);
        }
        return entity;
    }

    @Override
    public List<MessageEntity> getAllMessages(String sender, String receiver) {
        List<MessageEntity> list;
        try(Session session = sessionFactory.openSession()){
            Query query = sessionFactory.getCurrentSession().createQuery("from MessageEntity m where m.sender=:sender and m.recipient=:receiver");
            query.setParameter("sender", sender);
            query.setParameter("receiver", receiver);
            list = (List<MessageEntity>) query.getResultList();
        }
        catch (NoResultException exp) {
            list = null;
        } catch (RuntimeException exp) {
            throw new BusinessLogicException("Exception in DB: " + exp.getMessage(), ErrorCode.DATABASE_ERROR);
        }
        return list;
    }

    @Override
    public List<MessageEntity> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp) {
        List<MessageEntity> result;
        String queryString = "SELECT m FROM MessageEntity m WHERE " +
                "m.sender = :sender" +
                "m.recipient = :receiver" +
                "m.message_timestamp >= :sinceTimestamp";

        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery(queryString, MessageEntity.class)
                    .setParameter(":sender", sender)
                    .setParameter(":receiver", receiver)
                    .setParameter(":sinceTimestamp", sinceTimestamp)
                    .getResultList();
        }

        if (result == null) {
            throw new BusinessLogicException("Messages with" +
                    " sender = " + sender +
                    " receiver = " + receiver +
                    " sinceTimestamp = " + sinceTimestamp +
                    "was not found", ErrorCode.MESSAGE_NOT_FOUND);
        }

        return result;
    }

    @Override
    public MessageEntity saveMessage(MessageEntity message) {
        MessageEntity result;

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
                session.save(message);
            transaction.commit();
        }

        result = getMessage(message.getId());
        if (result == null) {
            throw new BusinessLogicException("Error saving " + message, ErrorCode.DATABASE_ERROR);
        }

        return result;
    }

    @Override
    public Collection<MessageEntity> saveAllMessages(Collection<MessageEntity> messages) {
        return null;
    }

    @Override
    public void deleteMessage(String messageId) {

    }
}
