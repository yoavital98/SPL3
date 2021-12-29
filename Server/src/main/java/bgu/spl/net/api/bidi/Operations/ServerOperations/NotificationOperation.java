package bgu.spl.net.api.bidi.Operations.ServerOperations;

import bgu.spl.net.api.bidi.OperationServer;

public class NotificationOperation extends OperationServer {
    private byte notificationType;
    private String postingUser;
    private String content;
    public NotificationOperation(short opCode, byte notificationType, String postingUser, String content) {
        super(opCode);
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        this.content = content;
    }
    public byte getNotificationType() {
        return notificationType;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }
}
