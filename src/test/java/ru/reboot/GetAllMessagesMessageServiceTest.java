package ru.reboot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.reboot.dao.MessageRepositoryImpl;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.MessageInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;
import ru.reboot.service.MessageServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GetAllMessagesMessageServiceTest {
    private MessageServiceImpl messageServiceService;
    private MessageRepositoryImpl messageRepository;

    @Before
    public void Init(){
        messageRepository = Mockito.mock(MessageRepositoryImpl.class);
        messageServiceService = Mockito.spy(new MessageServiceImpl());
        messageServiceService.setMessageRepository(messageRepository);
    }

    @Test
    public void negativeBadSender(){
        String sender = null;
        String receiver = "receiver";

        try{
            messageServiceService.getAllMessages(sender,receiver);
            Assert.fail();
        }
        catch (BusinessLogicException exception){
            Assert.assertEquals(exception.getCode(), ErrorCode.ILLEGAL_ARGUMENT);
        }
    }
    @Test
    public void negativeBadReceiver(){
        String sender = "sender";
        String receiver = null;

        try{
            messageServiceService.getAllMessages(sender,receiver);
            Assert.fail();
        }
        catch (BusinessLogicException exception){
            Assert.assertEquals(exception.getCode(), ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    @Test
    public void negativeNoMessages(){
        String sender = "sender";
        String receiver = "receiver";
        Mockito.when(messageRepository.getAllMessages(sender,receiver)).thenReturn(null);
        try{
            messageServiceService.getAllMessages(sender,receiver);
            Assert.fail();
        }
        catch (BusinessLogicException exception){
            Assert.assertEquals(exception.getCode(),ErrorCode.MESSAGE_NOT_FOUND);
        }
    }

    @Test
    public void positiveGetMessage(){
        String sender = "sender";
        String receiver = "receiver";
        List<MessageEntity> entityList = new ArrayList<>();
        MessageEntity entity1 = new MessageEntity.Builder()
                .setId("id1")
                .setSender("sender2")
                .setRecipient("recipient1")
                .setContent("content1")
                .setMessageTimestamp(LocalDateTime.now())
                .setLastAccessTime(LocalDateTime.now())
                .build();

        MessageEntity entity2 = new MessageEntity.Builder()
                .setId("id2")
                .setSender("sender2")
                .setRecipient("recipient2")
                .setContent("content2")
                .setMessageTimestamp(LocalDateTime.now())
                .setLastAccessTime(LocalDateTime.now())
                .build();

        entityList.add(entity1);
        entityList.add(entity2);

        Mockito.when(messageRepository.getAllMessages(sender,receiver)).thenReturn(entityList);
        ArrayList<MessageInfo> infoArrayList = (ArrayList<MessageInfo>) messageServiceService.getAllMessages(sender,receiver);

        for(int i=0;i<=infoArrayList.size()-1;i++){
            Assert.assertEquals(infoArrayList.get(i).getId(),entityList.get(i).getId());
            Assert.assertEquals(infoArrayList.get(i).getRecipient(),entityList.get(i).getRecipient());
            Assert.assertEquals(infoArrayList.get(i).getContent(),entityList.get(i).getContent());
            Assert.assertEquals(infoArrayList.get(i).getMessageTimestamp(),entityList.get(i).getMessageTimestamp());
            Assert.assertEquals(infoArrayList.get(i).getLastAccessTime(),entityList.get(i).getLastAccessTime());
        }

    }
}
