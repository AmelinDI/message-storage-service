package ru.reboot.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class MessageEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "content")
    private String content;

    @Column(name = "message_timestamp")
    private LocalDateTime messageTimestamp;

    @Column(name = "last_access_time")
    private LocalDateTime lastAccessTime;
}