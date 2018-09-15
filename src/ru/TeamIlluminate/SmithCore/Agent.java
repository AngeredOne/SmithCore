package ru.TeamIlluminate.SmithCore;

import java.net.Socket;

 abstract class Agent {

    protected Protocol protocol;
    protected String UID;

     Boolean isConnected;

     Agent(String UID)
    {
        this.UID = UID;
    }

     void InitRecieve() {
        protocol.Receive();
    }

}
