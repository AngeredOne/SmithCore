package ru.TeamIlluminate.SmithCore;

class StateManager {

    private static StateManager stateManager;

    public static StateManager instance() {
        if (stateManager == null)
            stateManager = new StateManager();
        return stateManager;
    }


    private StateManager()
    {
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
