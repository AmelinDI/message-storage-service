package ru.reboot.dto;

import java.time.LocalDateTime;

public class MessageInfo {

    private String id;
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime messageTimestamp;
    private LocalDateTime lastAccessTime;
}
