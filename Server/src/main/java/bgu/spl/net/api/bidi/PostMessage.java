package bgu.spl.net.api.bidi;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PostMessage implements Message {
    private List<String> userRecipientList;
    private final String message;
    private final String sender;
    private final ConcurrentHashMap<String, Boolean> recipientsRecieved;

    public PostMessage(List<String> userList, String message, String sender) {
        this.userRecipientList = userList;
        this.message = message;
        this.sender = sender;
        this.recipientsRecieved = new ConcurrentHashMap<>();
        for(String user : userList){
            recipientsRecieved.put(user, false);
        }
    }
    public List<String> getUserRecipientList() {
        return userRecipientList;
    }

    public void messageReceived(String userRead)
    {
        recipientsRecieved.replace(userRead,false,true);
    }
    public boolean isReceived(String userRead) {
        return recipientsRecieved.get(userRead).booleanValue();
    }
    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}