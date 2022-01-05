package bgu.spl.net.api.bidi.Operations.ClientOperations;

import bgu.spl.net.api.bidi.OperationClient;

public class LoginOperation extends OperationClient {
    private String userName;
    private String password;
    private boolean captcha;

    public LoginOperation(short opCode) {
        super(opCode);
        this.userName = "";
        this.password = "";
        this.captcha = false;
    }

    @Override
    public boolean pushByte(byte nextByte) {
        if(nextByte == ';')
            return true;
        if(nextByte=='\0')
        {
            if(getUserName().equals("")){
                userName = bytesToString();
            }
            else if(password.equals("")){
                this.password = bytesToString();
            }
        }
        else if(password.equals(""))
            pushNextByte(nextByte);
        else if(nextByte=='1')
            captcha=true;
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
    public boolean getCaptcha() {
        return captcha;
    }


}
