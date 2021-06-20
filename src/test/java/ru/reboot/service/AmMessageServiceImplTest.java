package ru.reboot.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.reboot.dao.MessageRepositoryImpl;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.MessageInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AmMessageServiceImplTest {
    @InjectMocks
    MessageServiceImpl messageService;

    @Mock
    MessageRepositoryImpl messageRepository;

    @Test
    public void getAllMessages() {
        LocalDateTime testDateTime = LocalDateTime.of(2001, 3, 3,1, 1);
        List<MessageEntity> messageEntityList = new ArrayList<>();
        MessageEntity messageEntity01 = new MessageEntity.Builder()
                .setId("1001")
                .setContent("Content01")
                .setSender("SenderNew")
                .setRecipient("ReceiverNew")
                .setMessageTimestamp(testDateTime)
                .build();
        messageEntityList.add(messageEntity01);

        List<MessageInfo> messageInfoList = new ArrayList<>();
        MessageInfo messageInfo01 = new MessageInfo.Builder()
                .setId("1001")
                .setContent("Content01")
                .setSender("SenderNew")
                .setRecipient("ReceiverNew")
                .setMessageTimestamp(testDateTime)
                .build();
        messageInfoList.add(messageInfo01);
        Mockito.when(messageRepository.getAllMessages("SenderNew", "ReceiverNew", testDateTime)).thenReturn(messageEntityList);

        // 1. verify positive version
        Assert.assertEquals(messageInfoList.toString(), messageService.getAllMessages("SenderNew", "ReceiverNew", testDateTime).toString());


        // 2. Try empty "sender" parameter
        try {
            messageService.getAllMessages("", "ReceiverNew", testDateTime);
            fail();
        } catch (BusinessLogicException ble) {
            assertEquals(ErrorCode.ILLEGAL_ARGUMENT, ble.getCode());
        }

    }


    @Test
    public void saveMessage() {
        // prepare
        MessageInfo messageInfo01 = new MessageInfo.Builder()
                .setId("1001")
                .setContent("Content01")
                .build();
        MessageEntity messageEntity01 = new MessageEntity.Builder()
                .setId("1001")
                .setContent("Content01")
                .build();
        Mockito.when(messageRepository.saveMessage(any(MessageEntity.class))).thenReturn(messageEntity01);

        // verify positive Save
        MessageInfo returnedMessage = messageService.saveMessage(messageInfo01);
        assertEquals("Content01", returnedMessage.getContent());

        // verify save of empty message
        messageInfo01.setContent("");
        try {
            messageService.saveMessage(messageInfo01);
             fail(); // Если попали сюда, значит не Exception
        } catch (BusinessLogicException ble) {
            assertEquals(ErrorCode.ILLEGAL_ARGUMENT, ble.getCode());
        }

        assertEquals("Content01", returnedMessage.getContent());
    }
}