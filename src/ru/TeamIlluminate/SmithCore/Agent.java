package ru.TeamIlluminate.SmithCore;

import jdk.nashorn.internal.objects.annotations.Constructor;

import java.net.Socket;

public abstract class Agent {

    protected SmithProtocol protocol;
    protected Socket socket;
    protected NetworkStream strean;
    protected String UID;
    protected StateManager SM;

    public Boolean isConnected;

    public Agent(Socket socket, String UID)
    {
        this.socket = socket;
        this.UID = UID;
    }

    public Socket getSocket() {
        return socket;
    }



}
