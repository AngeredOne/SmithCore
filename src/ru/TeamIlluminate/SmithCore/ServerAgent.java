package ru.TeamIlluminate.SmithCore;

import java.net.Socket;
import java.util.stream.Stream;

 class ServerAgent {
    private Socket socket;
    private NetworkStream stream;
    private SmithProtocol protocol;
    private String UID;
    public boolean isConnected;

    public ServerAgent (Socket socket, String UID) {}
    public void initSend(Byte[] data) {}
    public byte[] initRecieve() {return null;}

    private void agentDisconnected() {}
    private void connectHandler() {}

}
