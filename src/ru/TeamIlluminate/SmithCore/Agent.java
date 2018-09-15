package ru.TeamIlluminate.SmithCore;

import java.net.Socket;

public abstract class Agent {

    protected Protocol protocol;
    protected String UID;

    public Boolean isConnected;

    public Agent(String UID)
    {
        this.UID = UID;
    }

    public void InitRecieve() {
        protocol.Receive();
    }

}
