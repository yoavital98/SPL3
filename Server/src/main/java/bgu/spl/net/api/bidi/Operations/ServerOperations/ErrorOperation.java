package bgu.spl.net.api.bidi.Operations.ServerOperations;

import bgu.spl.net.api.bidi.OperationServer;

public class ErrorOperation extends OperationServer {
    private short messageOpCode;
    public ErrorOperation(short opCode, short messageOpCode) {
        super(opCode);
        this.messageOpCode = messageOpCode;
    }
    public short getMessageOpCode() {
        return messageOpCode;
    }

}
