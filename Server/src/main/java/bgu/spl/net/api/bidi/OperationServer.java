package bgu.spl.net.api.bidi;

public abstract class OperationServer implements Operation {
    private final short opCode;

    public OperationServer(short opCode) {
        this.opCode = opCode;

    }
    public short getOpCode() {
        return opCode;
    }


}
