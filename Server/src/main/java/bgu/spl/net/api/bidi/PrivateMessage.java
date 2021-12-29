package bgu.spl.net.api.bidi;

public class PrivateMessage implements Message{
    private final String message;
    private final User sender;
    private final User recipient;
    private final String date;
    private boolean received;
    public PrivateMessage(String message, User sender, User recipient, String date) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
        received = false;
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

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public String getDate() {
        return date;
    }
}
