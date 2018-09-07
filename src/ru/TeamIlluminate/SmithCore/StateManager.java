package ru.TeamIlluminate.SmithCore;

class StateManager {

    public static StateManager stateManager;

    public StateManager()
    {
        stateManager = this;
    }

    enum RETURN_CODE {
        // Exception while sending
        SendException,
        // Exception while receiving
        ReceiveException,
        // Socket sended package with a disconnection flag
        DissconectionFlag,
        // Protocol send function end sending
        SendOK,
        // Protocol receive function end receiving
        ReceiveOK
    }

    public void AgentDisconnected(Agent agent, boolean isFullDisconnected)
    {
        //call event and delegate agent
    }

    public void ServerDisconnected()
    {

    }

    public void AgentReconnected(Agent agent)
    {

    }

    public void ServerReconnected()
    {

    }

    public void BytesRecived(Byte[] bytes)
    {

    }

    public void ReconnectThreadIsAborted()
    {

    }
}
