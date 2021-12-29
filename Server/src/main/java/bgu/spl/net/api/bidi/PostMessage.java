package bgu.spl.net.api.bidi;

import java.util.List;

public class PostMessage implements Message {
    private final List<String> userRecipientList;
    private final String message;
    private final String sender;

    public PostMessage(List<String> userList, String message, String sender) {
        this.userRecipientList = userList;
        this.message = message;
        this.sender = sender;
    }
    public List<String> getUserRecipientList() {
        return userRecipientList;
    }

}
