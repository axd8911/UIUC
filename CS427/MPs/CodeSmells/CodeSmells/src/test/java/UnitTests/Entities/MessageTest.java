package UnitTests.Entities;

import Entities.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    @Test
    public void getMessageTest(){
        String msg = "something";
        Message m = new Message(MessageType.CheckIn, msg);
        assertEquals(msg,m.getContent());

    }

    @Test
    public void getTypeTest(){
        Message m = new Message(MessageType.CheckIn, "dummy");
        assertEquals(MessageType.CheckIn, m.getType());
    }
}
