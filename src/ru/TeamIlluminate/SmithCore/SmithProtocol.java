package ru.TeamIlluminate.SmithCore;

import com.sun.xml.internal.ws.commons.xmlutil.Converter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class SmithProtocol {
    private NetworkStream stream;
    private List<SmithPackage> packageList;
    private int errorPackage = -1;

    public SmithProtocol(NetworkStream _stream)
    {
        this.stream = _stream;
    }

    public void send (Byte[] data)
    {
        packageList = formPackages((data));

        packageList.forEach(sPack -> {

            try {

                stream.output.write(sPack.getBytes());
                stream.output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public void resend() {}

    public byte[] recieve () {
        return new byte[]{};
    }

    private List<SmithPackage> formPackages(Byte[] data) {return null;}

    //Something "stream" instead for network stream
    private byte[] parsePackages() {

        return null;
    }

}
