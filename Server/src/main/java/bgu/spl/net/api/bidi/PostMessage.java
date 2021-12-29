package bgu.spl.net.api.bidi;

import java.util.List;

public class PostMessage implements Message {
    private final List<String> userRecipientList;
    private final String message;
    private final String sender;
    private boolean received;

    public PostMessage(List<String> userList, String message, String sender) {
        this.userRecipientList = userList;
        this.message = message;
        this.sender = sender;
        this.received = false;
    }
    public List<String> getUserRecipientList() {
        return userRecipientList;
    }

    public void messageReceived()
    {
        received = true;
    }
    public boolean isReceived() {
        return received;
    }
    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}