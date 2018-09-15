package ru.TeamIlluminate.SmithCore;

import java.io.*;

class NetworkStream {
     InputStream input;
     OutputStream output;

     NetworkStream(InputStream iStream, OutputStream oStream)
    {
        this.input = iStream;
        this.output = oStream;
    }
}
