package ru.TeamIlluminate.SmithCore;

import java.io.*;

class SmithPackage {

    public FlagByte flag = new FlagByte();

    //Max range - 63 bytes
    public byte[] data = new byte[60];

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


    public SmithPackage getPackage(byte[] bytes) throws IOException
    {
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            obj = in.readObject();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return (SmithPackage)obj;
    }
}
