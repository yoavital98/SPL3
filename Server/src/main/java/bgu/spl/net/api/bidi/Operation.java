package bgu.spl.net.api.bidi;

public interface Operation {
    public short getOpCode();
    public short getMessageOpCode();
    public String getMessage();
    public String getUserName();
}
