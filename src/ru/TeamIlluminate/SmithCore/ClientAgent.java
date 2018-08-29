package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

class ClientAgent {
    private Socket socket;
    private SocketAddress serverAddress;
    private SmithProtocol protocol;
    private NetworkStream stream;
    //Will get from server
    private String UID;

    public ClientAgent() {
        this.socket = new Socket();
    }
    public void connect(SocketAddress server) {
        try {
            serverAddress = server;
            socket.connect(server);
            stream = new NetworkStream(socket.getInputStream(), socket.getOutputStream());
        } catch (IOException e) {
            //error when try connect
            e.printStackTrace();
        }
    }
    public void initSend(Byte[] data) {
        if (getConnectionStatus()) {
                //WorkWithData
                //
        } else agentDisconnected();
    }
    public byte[] initRecieve() {
        if (getConnectionStatus()) {
                //WorkWithData
                //
        } else agentDisconnected();
        return null;
    }
    public boolean getConnectionStatus() {
        return socket.isConnected() & !socket.isClosed();
    }
    private void agentDisconnected() {

    }
    private void reconnectAttemp() {}
}
