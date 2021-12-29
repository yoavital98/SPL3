package bgu.spl.net.api.bidi;

public class OperationServer implements Operation {
    private final short opCode;
    private final short messageOpCode;
    private final String message;
    private final String userName;

    public OperationServer(short opCode,short messageOpCode, String message, String userName) {
        this.opCode = opCode;
        this.messageOpCode = messageOpCode;
        this.message = message;
        this.userName = userName;
    }
    public short getOpCode() {
        return opCode;
    }
    public String getMessage() {
        return message;
    }
    public short getMessageOpCode() {
        return messageOpCode;
    }
    public String getUserName() {
        return userName;
    }
}
