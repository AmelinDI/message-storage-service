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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
        try (Session session = sessionFactory.openSession()) {

            Query<MessageEntity> query = session.createQuery("from MessageEntity m where m.id=:id", MessageEntity.class);
            query.setParameter("id", messageId);

            List<MessageEntity> entities = query.list();
            return !entities.isEmpty() ? entities.get(0) : null;
        } catch (Exception exp) {
            throw new BusinessLogicException("Exception in DB: " + exp.getMessage(), ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<MessageEntity> getAllMessages(String user) {

        try (Session session = sessionFactory.openSession()) {

            Query<MessageEntity> query = session.createQuery("from MessageEntity m where (m.sender=:user or m.recipient=:user)", MessageEntity.class);
            query.setParameter("user", user);
            return query.list();
        } catch (Exception ex) {
            String message = String.format("Failed to .getAllMessages user=%s error=%s", user, ex.toString());
            throw new BusinessLogicException(message, ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<MessageEntity> getAllMessages(String sender, String receiver) {

        try (Session session = sessionFactory.openSession()) {

            Query<MessageEntity> query = session.createQuery("from MessageEntity m where (m.sender=:sender and m.recipient=:receiver) or (m.sender=:receiver and m.recipient=:sender)", MessageEntity.class);
            query.setParameter("sender", sender);
            query.setParameter("receiver", receiver);
            return query.list();
        } catch (Exception ex) {
            String message = String.format("Failed to .getAllMessages sender=%s receiver=%s error=%s", sender, receiver, ex.toString());
            throw new BusinessLogicException(message, ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<MessageEntity> getAllMessages(String sender, String receiver, LocalDateTime sinceTimestamp) {

        try (Session session = sessionFactory.openSession()) {

            Query<MessageEntity> query = session.createQuery("from MessageEntity m where " +
                    "((m.sender=:sender and m.recipient=:receiver) or (m.sender=:receiver and m.recipient=:sender)) and " +
                    "m.messageTimestamp >= :sinceTimestamp", MessageEntity.class);
            query.setParameter("sender", sender);
            query.setParameter("receiver", receiver);
            query.setParameter("sinceTimestamp", sinceTimestamp);

            return query.list();
        } catch (Exception ex) {
            String message = String.format("Failed to .getAllMessages sender=%s receiver=%s sinceTimestamp=%s error=%s", sender, receiver, sinceTimestamp, ex.toString());
            throw new BusinessLogicException(message, ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public MessageEntity saveMessage(MessageEntity message) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(message);
            transaction.commit();

            MessageEntity result = getMessage(message.getId());
            if (result == null) {
                throw new BusinessLogicException("Failed to get message message=" + message, ErrorCode.DATABASE_ERROR);
            }
            return result;
        } catch (Exception ex) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new BusinessLogicException("Failed to .saveMessage message=" + message + " error=" + ex.toString(), ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Collection<MessageEntity> saveAllMessages(Collection<MessageEntity> messages) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            for (MessageEntity message : messages) {
                session.saveOrUpdate(message);
            }
            transaction.commit();

            return messages.stream()
                    .map(entity -> getMessage(entity.getId()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new BusinessLogicException("Failed to .saveAllMessages messages=" + messages + " error=" + ex.toString(), ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteMessage(String messageId) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setId(messageId);
            session.delete(messageEntity);

            transaction.commit();
        } catch (Exception ex) {

            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new BusinessLogicException("Failed to .deleteMessage error=" + ex.toString(), ErrorCode.DATABASE_ERROR);
        }
    }
}
