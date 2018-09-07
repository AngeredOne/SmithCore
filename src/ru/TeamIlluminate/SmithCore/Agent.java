package ru.TeamIlluminate.SmithCore;

import jdk.nashorn.internal.objects.annotations.Constructor;

import java.net.Socket;

public abstract class Agent {

    protected Protocol protocol;
    protected Socket socket;
    protected NetworkStream stream;
    protected String UID;

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
