package bgu.spl.net.api.bidi.Operations.ClientOperations;
import bgu.spl.net.api.bidi.OperationClient;

public class LogStatOperation extends OperationClient{

    public LogStatOperation(short opCode) {
        super(opCode);
    }

    @Override
    public boolean pushByte(byte nextByte) {
        if(nextByte == ';')
            return true;
        return false;
    }

}
