package ru.TeamIlluminate.SmithCore;

import java.io.*;

class SmithPackage {

     FlagByte flag = new FlagByte();

    //Max range - 63 bytes
     byte[] data = new byte[60];

     SmithPackage()
    {
        //init package data by zero-bytes
        for(int i = 0; i < 60; ++i)
        {
            data[i] = (byte)0;
        }
    }

     SmithPackage(FlagByte flag, byte[] data)
    {
        this.flag = flag;
        this.data = data;
    }


     byte[] getBytes()
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
