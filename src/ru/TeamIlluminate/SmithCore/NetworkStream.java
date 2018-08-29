package ru.TeamIlluminate.SmithCore;

import java.io.*;

class NetworkStream {
    public InputStream input;
    public OutputStream output;

    public NetworkStream(InputStream iStream, OutputStream oStream)
    {
        this.input = iStream;
        this.output = oStream;
    }
}
