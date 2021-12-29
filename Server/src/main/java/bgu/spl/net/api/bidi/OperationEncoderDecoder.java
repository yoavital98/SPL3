package bgu.spl.net.api.bidi;
import bgu.spl.net.api.MessageEncoderDecoder;

import java.util.Arrays;

public class OperationEncoderDecoder implements MessageEncoderDecoder<Operation> {
    int length = 0;
    private byte[] bytes = new byte[1 << 10];
    short opCode=-1;
    @Override
    public Operation decodeNextByte(byte nextByte) {
        if (opCode==-1)
        {
            pushByte(nextByte);
            if(length==2)
                opCode= bytesToShort(Arrays.copyOfRange(bytes, 0,1));
        }
        else
        {
            switch(opCode) {
                case 1:


                case 2:

                case 3:

                case 4:

                case 5:

                case 6:

                case 7:

                case 8:

                case 12:
            }
        }
            return null;
    }
    private void pushByte(byte nextByte) {
        if (length >= bytes.length) {
            bytes = Arrays.copyOf(bytes, length * 2);
        }

        bytes[length++] = nextByte;
    }
    @Override
    public byte[] encode(Operation message) {
        short opCode = message.getOpCode();
        byte[] bArrOpCode = shortToBytes(opCode);
        byte[] bArrMessage;
        switch (opCode)
        {
            case 9:
                short notificationType;
                if(message.getMessageOpCode()==6)
                    notificationType = 0;
                else
                    notificationType = 1;
                byte notificationByte = (byte)notificationType;
                byte[] bArrPostingUser = message.getUserName().getBytes();
                byte[] bArrContent = message.getMessage().getBytes();
                byte[] bArrNotification = new byte[5+bArrPostingUser.length+bArrContent.length];
                bArrNotification[0] = bArrOpCode[0];
                bArrNotification[1] = bArrOpCode[1];
                bArrNotification[2] = notificationByte;
                for(int i=0;i<bArrPostingUser.length;i++)
                    bArrNotification[i+3] = bArrPostingUser[i];
                bArrNotification[3+bArrPostingUser.length] = (byte)0;
                for(int i=0;i<bArrContent.length;i++)
                    bArrNotification[i+4+bArrPostingUser.length] = bArrContent[i];
                bArrNotification[bArrNotification.length-1] = (byte)0;
                return bArrNotification;

            case 10:
                bArrMessage = shortToBytes(message.getMessageOpCode());
                byte[] bArrOptional = message.getMessage().getBytes();
                byte[] bArrACK = new byte[4+bArrOptional.length];
                bArrACK[0] = bArrOpCode[0];
                bArrACK[1] = bArrOpCode[1];
                bArrACK[2] = bArrMessage[0];
                bArrACK[3] = bArrMessage[1];
                for(int i=0;i<bArrACK.length;i++)
                    bArrACK[i+4] = bArrOptional[i];
                return bArrACK;

            case 11:
                bArrMessage = shortToBytes(message.getMessageOpCode());
                byte[] bArrErr = new byte[4];
                bArrErr[0] = bArrOpCode[0];
                bArrErr[1] = bArrOpCode[1];
                bArrErr[2] = bArrMessage[0];
                bArrErr[3] = bArrMessage[1];
                return bArrErr;
        }
        return null;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
}
