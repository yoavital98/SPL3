package bgu.spl.net.api.bidi.Operations.ClientOperations;
import bgu.spl.net.api.bidi.OperationClient;

public class BlockOperation extends OperationClient{
    private String userName;

    public BlockOperation(short opCode) {
        super(opCode);
        this.userName = "";
    }

    @Override
    public boolean pushByte(byte nextByte) {
        if(nextByte == ';')
            return true;
        if(nextByte==(byte)0)
            userName = bytesToString();
        pushNextByte(nextByte);
        return false;
    }

    public String getUserName() {
        return userName;
    }
}
