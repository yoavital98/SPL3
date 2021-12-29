package bgu.spl.net.api.bidi.Operations.ClientOperations;
import bgu.spl.net.api.bidi.OperationClient;

public class PostMessageOperation extends OperationClient {
    private String content;

    public PostMessageOperation(short opCode) {
        super(opCode);
        this.content = "";
    }

    @Override
    public boolean pushByte(byte nextByte) {
        if(nextByte == ';')
            return true;
        if(nextByte==(byte)0)
            content = bytesToString();
        else
            pushNextByte(nextByte);
        return false;
    }

    public String getContent() {
        return content;
    }

}
