package ru.TeamIlluminate.SmithCore;

public class FlagByte {

    public boolean Disconnect = false;
    public boolean Resended = false;
    public boolean EndTransmission = false;
    public boolean Empty = false;

    public byte[] getBytes()
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (Disconnect?1:0);
        bytes[1] = (byte) (Resended?1:0);
        bytes[2] = (byte) (EndTransmission?1:0);
        bytes[3] = (byte) (Empty?1:0);
        return bytes;
    }

    public FlagByte getFlags(byte[] bytes)
    {
        Disconnect = (boolean)(bytes[0] == 1 ? true:false);
        Resended = (boolean)(bytes[0] == 1 ? true:false);
        EndTransmission = (boolean)(bytes[0] == 1 ? true:false);
        Empty = (boolean)(bytes[0] == 1 ? true:false);
        return this;
    }
}
