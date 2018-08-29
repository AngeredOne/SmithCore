package ru.TeamIlluminate.SmithCore;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Stream;

 class SmithProtocol {
    private List<SmithPackage> packageList;
    private int errorPackage = -1;

    public void send (Byte[] data, NetworkStream stream) {}

    public void resend() {}

    public byte[] recieve (NetworkStream stream) {
        return new byte[]{};
    }

    private List<SmithPackage> formPackages(Byte[] data) {return null;}

    //Something "stream" instead for network stream
    private byte[] parsePackages(NetworkStream stream) {

       return null;
    }

}
