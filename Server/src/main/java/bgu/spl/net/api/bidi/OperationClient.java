package bgu.spl.net.api.bidi;

public class OperationClient implements Operation {
    private final short opCode;
    private final String message;
    private final String userName;

    public OperationClient(short opCode, String message, String userName) {
        this.opCode = opCode;
        this.message = message;
        this.userName = userName;
    }
    public short getOpCode() {
        return opCode;
    }
    public short getMessageOpCode() {
        return -1;
    }
    public String getMessage() {
        return message;
    }
    public String getUserName() {
        return userName;
    }
}

