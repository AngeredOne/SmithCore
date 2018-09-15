package ru.TeamIlluminate.SmithCore;

 class FlagByte {

     boolean Disconnect = false;
     boolean Resended = false;
     boolean EndTransmission = false;
     byte pSize = 0;

     byte[] getBytes()
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (Disconnect?1:0);
        bytes[1] = (byte) (Resended?1:0);
        bytes[2] = (byte) (EndTransmission?1:0);
        bytes[3] = pSize;
        return bytes;
    }

     FlagByte getFlags(byte[] bytes)
    {
        Disconnect = (bytes[0] == 1);
        Resended = (bytes[1] == 1);
        EndTransmission = (bytes[2] == 1);
        pSize = bytes[3];
        return this;
    }
}
