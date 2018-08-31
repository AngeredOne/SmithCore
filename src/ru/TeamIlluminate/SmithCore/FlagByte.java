package ru.TeamIlluminate.SmithCore;

import com.sun.javafx.scene.layout.region.Margins;
import com.sun.xml.internal.ws.commons.xmlutil.Converter;

public class FlagByte {

    public boolean Disconnect = false;
    public boolean Resended = false;
    public boolean EndTransmission = false;
    public byte pSize = 0;

    public byte[] getBytes()
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (Disconnect?1:0);
        bytes[1] = (byte) (Resended?1:0);
        bytes[2] = (byte) (EndTransmission?1:0);
        bytes[3] = pSize;
        return bytes;
    }

    public FlagByte getFlags(byte[] bytes)
    {
        Disconnect = (bytes[0] == 1);
        Resended = (bytes[1] == 1);
        EndTransmission = (bytes[2] == 1);
        pSize = bytes[3];
        return this;
    }
}
