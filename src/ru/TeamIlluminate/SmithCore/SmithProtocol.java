package ru.TeamIlluminate.SmithCore;

import java.util.List;
import java.util.stream.Stream;

 class SmithProtocol {
    private List<SmithPackage> packageList;
    private int errorPackage = -1;

    public void send (Byte[] data) {}
    public void resend() {}
    //"out" in java?))))
    public int recieve (byte[] bytes) { return -1; }

    private List<SmithPackage> formPackages(Byte[] data) {return null;}
    //Something "stream" instead for network stream
    private byte[] parsePackages(Stream stream) {return null;}

}
