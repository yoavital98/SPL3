package bgu.spl.net.api.bidi;
import bgu.spl.net.api.bidi.Operations.ClientOperations.*;
import bgu.spl.net.api.bidi.Operations.ServerOperations.ACKOperation;
import bgu.spl.net.api.bidi.Operations.ServerOperations.ErrorOperation;
import bgu.spl.net.srv.bidi.ConnectionHandler;
import jdk.internal.foreign.AbstractCLinker;

import java.sql.Connection;

public class BGSProtocol implements BidiMessagingProtocol<Operation>{
    private UserController userController;
    private String userName;
    private ConnectionHandler<Operation> connection;
    private int connectionId;
    public BGSProtocol(UserController userController) {
        this.userController = userController;
        this.userName = "";
    }
    public void initiateConnection(ConnectionHandler<Operation> connection)
    {
        System.out.println("New Client Connected");
        this.connection = connection;
    }

    @Override
    public void start(int connectionId, Connections<Operation> connections) {
        this.connectionId = connectionId;
    }

    @Override
    public void process(Operation operation) {
        switch(operation.getOpCode()) {
            case 1:
                if (userController.regiser(((RegisterOperation) operation).getUserName(), ((RegisterOperation) operation).getPassword(), ((RegisterOperation) operation).getBirthday()))
                    connection.send(new ACKOperation((short) 10, (short) 1, "Registered Successfully"));
                else
                    connection.send(new ErrorOperation((short) 11, (short) 1));
            case 2:
                if (userController.login(((LoginOperation) operation).getUserName(), ((LoginOperation) operation).getPassword(), connectionId))
                    connection.send(new ACKOperation((short) 10, (short) 2, "Logged in Successfully"));
                else
                    connection.send(new ErrorOperation((short) 11, (short) 2));
            case 3:
                if (userController.logout(userName))
                    connection.send(new ACKOperation((short) 10, (short) 3, "Logged out Successfully"));
                else
                    connection.send(new ErrorOperation((short) 11, (short) 3));
            case 4:
                if (userController.followOrUnfollow(((FollowOperation) operation).getFollowOrUnfollow(), userName, ((FollowOperation) operation).getUserName())) {
                    if (((FollowOperation) operation).getFollowOrUnfollow() == 0)
                        connection.send(new ACKOperation((short) 10, (short) 4, ("Followed " + ((FollowOperation) operation).getUserName() + " Successfully")));
                    else
                        connection.send(new ACKOperation((short) 10, (short) 4, ("Unfollowed " + ((FollowOperation) operation).getUserName() + " Successfully")));
                } else
                    connection.send(new ErrorOperation((short) 11, (short) 4));
            case 5:
                if(userController.postMessage(userName, (((PostMessageOperation)operation).getContent())))
                    connection.send(new ACKOperation((short) 10, (short) 5, "Message has been posted Successfully"));
                else
                    connection.send(new ErrorOperation((short) 11, (short) 5));
            case 6:
                if(userController.sendPrivateMessage(userName, (((PrivateMessageOperation)operation).getUserName()), (((PrivateMessageOperation)operation).getContent()), (((PrivateMessageOperation)operation).getDateAndTime())))
                    connection.send(new ACKOperation((short) 10, (short) 5, "Message has been posted Successfully"));
                else
                    connection.send(new ErrorOperation((short) 11, (short) 5));
            case 7:
            case 8:
            case 12:
        }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

    /*private byte[] getLogStat(byte[] ackByte, byte[] logStatByte, byte[] ageByte, byte[] numberOfPostsByte, byte[] numberOfFollowingByte, byte[] numberOfFollowersByte) {
        byte[] logStat = new byte[12];
        logStat[0] = ackByte[0];
        logStat[1] = ackByte[1];
        logStat[2] = logStatByte[0];
        logStat[3] = logStatByte[1];
        logStat[4] = ageByte[0];
        logStat[5] = ageByte[1];
        logStat[6] = numberOfPostsByte[0];
        logStat[7] = numberOfPostsByte[1];
        logStat[8] = numberOfFollowingByte[0];
        logStat[9] = numberOfFollowingByte[1];
        logStat[10] = numberOfFollowersByte[0];
        logStat[11] = numberOfFollowersByte[1];
        return logStat;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }*/
}
