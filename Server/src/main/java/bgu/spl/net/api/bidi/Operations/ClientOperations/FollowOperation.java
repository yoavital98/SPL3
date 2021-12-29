package bgu.spl.net.api.bidi.Operations.ClientOperations;

import bgu.spl.net.api.bidi.OperationClient;

public class FollowOperation extends OperationClient {
    private String userName;
    private int followOrUnfollow;
    public FollowOperation(short opCode) {
        super(opCode);
        this.followOrUnfollow = -1;
    }

    @Override
    public boolean pushByte(byte nextByte) {
        if(followOrUnfollow==-1)
        {
            if(nextByte==(byte)0)
                followOrUnfollow = 0;
            else
                followOrUnfollow = 1;
        }
        else if(nextByte == ';') {
            userName = bytesToString();
            return true;
        }
        else
            pushNextByte(nextByte);
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public int getFollowOrUnfollow() {
        return followOrUnfollow;
    }

}
