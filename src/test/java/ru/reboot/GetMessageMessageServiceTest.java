package ru.reboot;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.reboot.dao.MessageRepositoryImpl;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;
import ru.reboot.service.MessageServiceImpl;
import ru.reboot.dto.MessageInfo;
import ru.reboot.dao.entity.MessageEntity;

import java.time.LocalDateTime;

public class GetMessageMessageServiceTest {
    private MessageServiceImpl messageServiceService;
    private MessageRepositoryImpl messageRepository;

    @Before
    public void Init(){
        messageRepository = Mockito.mock(MessageRepositoryImpl.class);
        messageServiceService = Mockito.spy(new MessageServiceImpl());
        messageServiceService.setMessageRepository(messageRepository);
    }

    @Test
    public void negativeBadMessageId(){
        String messageId = null;
        try{
            messageServiceService.getMessage(messageId);
            Assert.fail();
        }
        catch (BusinessLogicException exception){
            Assert.assertEquals(exception.getCode(),ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    @Test
    public void negativeNoMessage(){
        String messageId = "1";
        Mockito.when(messageRepository.getMessage(messageId)).thenReturn(null);
        try{
            messageServiceService.getMessage(messageId);
            Assert.fail();
        }
        catch (BusinessLogicException exception){
            Assert.assertEquals(exception.getCode(),ErrorCode.MESSAGE_NOT_FOUND);
        }
    }

    @Test
    public void positiveGetMessage(){
        String messageId = "id";
        MessageEntity entity = new MessageEntity.Builder()
                .setId("id")
                .setSender("sender")
                .setRecipient("recipient")
                .setContent("content")
                .setMessageTimestamp(LocalDateTime.now())
                .setLastAccessTime(LocalDateTime.now())
                .build();
        Mockito.when(messageRepository.getMessage(messageId)).thenReturn(entity);
        MessageInfo info = messageServiceService.getMessage(messageId);
        Assert.assertEquals(entity.getId(),info.getId());
        Assert.assertEquals(entity.getSender(),info.getSender());
        Assert.assertEquals(entity.getRecipient(),info.getRecipient());
        Assert.assertEquals(entity.getContent(),info.getContent());
        Assert.assertEquals(entity.getMessageTimestamp(),info.getMessageTimestamp());
        Assert.assertEquals(entity.getLastAccessTime(),info.getLastAccessTime());

    }

}
