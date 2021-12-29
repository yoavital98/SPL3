package bgu.spl.net.api.bidi.Operations.ServerOperations;
import bgu.spl.net.api.bidi.OperationServer;

public class ACKOperation extends OperationServer {
    private short messageOpCode;
    private String optional;
    public ACKOperation(short opCode, short messageOpCode, String optional) {
        super(opCode);
        this.messageOpCode = messageOpCode;
        this.optional = optional;
    }
    public short getMessageOpCode() {
        return messageOpCode;
    }

    public String getOptional() {
        return optional;
    }
}
