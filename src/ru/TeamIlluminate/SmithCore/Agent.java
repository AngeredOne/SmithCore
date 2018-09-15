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

    public void InitSend(Byte[] data) {
        StateManager.RETURN_CODE code = protocol.Send(data);
        if (code == StateManager.RETURN_CODE.SendException) {
            StateManager.instance().AgentDisconnected(this, false);
        }
    }

    public void InitRecieve() {

        StateManager.RETURN_CODE code = protocol.Receive();

        if (code == StateManager.RETURN_CODE.ReceiveException) {
            StateManager.instance().eventSystem.AgentDisconnected(this, false);
        } else if (code == StateManager.RETURN_CODE.DissconectionFlag) {
            StateManager.instance().eventSystem.AgentDisconnected(this, true);
        }
    }

}
