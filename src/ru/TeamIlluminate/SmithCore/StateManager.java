package ru.TeamIlluminate.SmithCore;

import java.util.ArrayList;
import java.util.Arrays;

class StateManager {

    private static StateManager stateManager;
    private CoreEventSystem eventSystem;

    private StateManager() {
        eventSystem = new CoreEventSystem();
    }

    public static StateManager instance() {
        if (stateManager == null)
            stateManager = new StateManager();
        return stateManager;
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

    public void AgentDisconnected(Agent agent)
    {

    }

    public void AgentReconnected(Agent agent)
    {

    }

    public void AgentKicked(Agent agent)
    {

    }

    public void CommunicationException(String message, Protocol protocol, Integer packNumber)
    {
        String info = String.format(
                "Exception while reading receive stream!" +
                        "\n" +
                        "Exception message: {0}" +
                        "\n" +
                        "Protocol: {1}" +
                        "\n" +
                        "Package number: {2}", message, protocol.getClass().getName(), packNumber);
    }

    public void ReceiverProvideBytes(ArrayList<Byte> bytes)
    {
        eventSystem.BytesRecieved(bytes);
        //Provide some more info
    }
}
