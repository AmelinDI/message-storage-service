package ru.reboot.dto;

import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dao.entity.MessageEntity.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MessageInfo {

    private String id;
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime messageTimestamp;
    private LocalDateTime lastAccessTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(LocalDateTime messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", content='" + content + '\'' +
                ", messageTimestamp=" + messageTimestamp +
                ", lastAccessTime=" + lastAccessTime +
                '}';
    }

    public static class Builder {
        private MessageInfo obj;

        public Builder() {
            obj = new MessageInfo();
        }

        public Builder setId(String id) {
            obj.id = id;
            return this;
        }

        public Builder setSender(String sender) {
            obj.sender = sender;
            return this;
        }

        public Builder setRecipient(String recipient) {
            obj.recipient = recipient;
            return this;
        }

        public Builder setContent(String content) {
            obj.content = content;
            return this;
        }

        public Builder setMessageTimestamp(LocalDateTime messageTimestamp) {
            obj.messageTimestamp = messageTimestamp;
            return this;
        }

        public Builder setLastAccessTime(LocalDateTime lastAccessTime) {
            obj.lastAccessTime = lastAccessTime;
            return this;
        }

        public MessageInfo build() {
            return obj;
        }
    }
}
