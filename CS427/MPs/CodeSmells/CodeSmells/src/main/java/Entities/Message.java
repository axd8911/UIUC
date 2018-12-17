package Entities;

public class Message {

    private MessageType type;

    public MessageType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    private String content;

    public Message(MessageType type, String msg) {
        this.type = type;
        this.content = msg;
    }

}
