package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

class ClientAgent extends Agent {

    private Socket socket;

    public ClientAgent() {
        super("");
        socket = new Socket();
    }

    public void connect(SocketAddress server) {
        try {
            socket.connect(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initSend(Byte[] data) {
        if (getConnectionStatus()) {
            protocol.Send(data);
        } else agentDisconnected();
    }

    public byte[] initRecieve() {
        if (getConnectionStatus()) {
            protocol.Receive();
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