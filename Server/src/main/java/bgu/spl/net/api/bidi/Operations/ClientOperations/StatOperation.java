package bgu.spl.net.api.bidi.Operations.ClientOperations;

import bgu.spl.net.api.bidi.OperationClient;

import java.util.ArrayList;
import java.util.List;

public class StatOperation extends OperationClient {
    private final List<String> userNamesList;
    public StatOperation(short opCode) {
        super(opCode);
        userNamesList = new ArrayList<>();
    }

    @Override
    public boolean pushByte(byte nextByte) {
        if(nextByte ==';')
            return true;
        if(nextByte =='|' || nextByte== (byte)0)
            userNamesList.add(bytesToString());
        else
            pushNextByte(nextByte);
        return false;
    }
    public List<String> getUserNamesList() {
        return userNamesList;
    }
}
