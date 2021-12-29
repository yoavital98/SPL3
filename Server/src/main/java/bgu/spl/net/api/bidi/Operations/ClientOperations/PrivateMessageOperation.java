package bgu.spl.net.api.bidi.Operations.ClientOperations;

import bgu.spl.net.api.bidi.OperationClient;

public class PrivateMessageOperation extends OperationClient {
    private String userName;
    private String content;
    private String dateAndTime;
    public PrivateMessageOperation(short opCode) {
        super(opCode);
        this.userName = "";
        this.content = "";
        this.dateAndTime = "";
    }

    @Override
    public boolean pushByte(byte nextByte) {
        if(nextByte == ';')
            return true;
        if(nextByte==(byte)0)
        {
            if(userName.equals("")){
                this.userName = bytesToString();
            }
            else if(content.equals("")){
                this.content = bytesToString();
            }
            else if(dateAndTime.equals("")){
                this.dateAndTime = bytesToString();
            }
        }
        else
            pushNextByte(nextByte);
        return false;
    }
    public String getUserName() {
        return userName;
    }
    public String getContent() {
        return content;
    }
    public String getDateAndTime() {
        return dateAndTime;
    }
}
