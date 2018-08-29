package ru.TeamIlluminate.SmithCore;

import java.net.Socket;
import java.util.stream.Stream;

class ClientAgent {
    private Socket socket;
    private Stream stream;
    private SmithProtocol protocol;
    private String UID;
    private boolean isConnected;

    public ClientAgent() {}
    public void connect(Socket server) {}
    public void initSend(Byte[] data) {}
    public byte[] initRecieve() {return null; }

    private void agentDisconnected() {}
    private void reconnectAttemp() {}
}
