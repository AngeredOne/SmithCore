package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;
import java.util.stream.Stream;

 class ServerAgent {
    private Socket socket;
    public Socket getSocket() {return socket;}
    private NetworkStream stream;
    private SmithProtocol protocol;
    private String UID;
    public boolean isConnected;

    public ServerAgent (Socket socket, String UID) throws IOException {
        this.socket = socket;
        this.UID = UID;
        this.protocol = new SmithProtocol(new NetworkStream(socket.getInputStream(), socket.getOutputStream()));
    }
    public void initSend(Byte[] data) {}
    public byte[] initRecieve() {return null;}

    private void agentDisconnected() {}
    private void connectHandler() {}

}
