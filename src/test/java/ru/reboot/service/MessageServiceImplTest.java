package ru.reboot.service;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.MessageInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageServiceImplTest {
    @InjectMocks
    private MessageServiceImpl messageService;

    @Mock
    private MessageRepository messageRepository;

    @Test
    public void positiveSaveAllMessagesTest() {
        MockitoAnnotations.initMocks(this);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId("10");
        when(messageRepository.saveMessage(any(MessageEntity.class))).thenReturn(messageEntity);
        List<MessageInfo> messageInfos = new ArrayList<>();
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setId("10");
        messageInfos.add(messageInfo);
        Assert.assertEquals(messageInfos, messageService.saveAllMessages(messageInfos));
    }

    @Test
    public void negativeSaveAllMessagesTest() {
        MockitoAnnotations.initMocks(this);
        try {
            messageService.saveAllMessages(null);
            Assert.fail();
        } catch (BusinessLogicException ex) {
            Assert.assertEquals(ErrorCode.ILLEGAL_ARGUMENT, ex.getCode());
        }
    }

    @Test
    public void positiveDeleteMessageTest() {
        MockitoAnnotations.initMocks(this);
        when(messageRepository.getMessage("1")).thenReturn(new MessageEntity());
        messageService.deleteMessage("1");
        verify(messageRepository).deleteMessage("1");
    }

    @Test
    public void negativeOneDeleteMessageTest() {
        MockitoAnnotations.initMocks(this);
        doThrow(new BusinessLogicException("User with that login or userId already exists", ErrorCode.MESSAGE_NOT_FOUND)).when(messageRepository).deleteMessage("2");
        try {
            messageService.deleteMessage("2");
        } catch (BusinessLogicException exception) {
            Assert.assertEquals(ErrorCode.MESSAGE_NOT_FOUND, exception.getCode());
        }
    }

    @Test
    public void negativeTwoDeleteMessageTest() {
        MockitoAnnotations.initMocks(this);
        try {
            messageService.deleteMessage(null);
            Assert.fail();
        } catch (BusinessLogicException exception) {
            Assert.assertEquals(ErrorCode.ILLEGAL_ARGUMENT, exception.getMessage());
        }
    }
}