package Terminals;

import Entities.Message;
import Entities.Queue;

public class MessageQueue extends Queue<Message> {

    private final static int MESSAGE_QUEUE_SIZE = 10;
    MessageQueue(){
        super(MESSAGE_QUEUE_SIZE);
    }



}
