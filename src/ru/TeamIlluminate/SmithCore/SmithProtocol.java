package ru.TeamIlluminate.SmithCore;

import java.nio.ByteBuffer;
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

    public void send (Byte[] data) {}

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
