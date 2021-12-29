package bgu.spl.net.api.bidi;

public class PrivateMessage implements Message{
    private final String message;
    private final User sender;
    private final User recipient;
    private final String date;

    public PrivateMessage(String message, User sender, User recipient, String date) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
    }
}
