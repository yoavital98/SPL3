package bgu.spl.net.api.bidi;

    public class BGSProtocol implements BidiMessagingProtocol<OperationServer>{
        @Override
        public void start(int connectionId, Connections<OperationServer> connections) {

        }

        @Override
        public void process(OperationServer message) {

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
