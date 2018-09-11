package ru.TeamIlluminate.SmithCore;

import java.io.*;

class SmithPackage {

    public FlagByte flag = new FlagByte();

    //Max range - 63 bytes
    public byte[] data = new byte[60];

    public SmithPackage()
    {
        //init package data by zero-bytes
        for(int i = 0; i < 60; ++i)
        {
            data[i] = (byte)0;
        }
    }

    public SmithPackage(FlagByte flag, byte[] data)
    {
        this.flag = flag;
        this.data = data;
    }


    public byte[] getBytes()
    {
        byte[] unionArray = new byte[64];
        byte[] flags = flag.getBytes();

        for(int i = 0; i < flags.length; ++i)
        {
            unionArray[i] = flags[i];
        }
        for(int i = flags.length; i < data.length; ++i)
        {
            unionArray[i] = data[i - 4];
        }
        return unionArray;
    }
}
