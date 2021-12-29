package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;

public class OperationEncoderDecoder implements MessageEncoderDecoder<String> {

    @Override
    public String decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(String message) {
        return new byte[0];
    }
}
