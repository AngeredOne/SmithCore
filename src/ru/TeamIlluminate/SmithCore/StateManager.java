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

    public void subcribeHandler(CoreEventHandler handler) {
        eventSystem.subscribe(handler);
    }

    public void AgentConnected(Agent agent) {
        eventSystem.AgentConnected(agent);
    }

    public void AgentDisconnected(Agent agent, boolean isFullDisconnected)
    {
        eventSystem.AgentLeave(agent);
        eventSystem.AgentDisconnected(agent);
    }

    public void AgentReconnected(Agent agent)
    {
        eventSystem.AgentReconnected(agent);
    }

    public void CommunicationException(String message, Protocol protocol, Integer packNumber, Agent agent)
    {
        String info = String.format(
                "Exception while reading receive stream!" +
                        "\n" +
                        "Exception message: {0}" +
                        "\n" +
                        "Protocol: {1}" +
                        "\n" +
                        "Package number: {2}",
                message, protocol.getClass().getName(), packNumber
        );
        eventSystem.CommunicationException(info);
        AgentDisconnected(agent, false);
    }

    public void ReconnectionThreadException(String message, Agent agent)
    {
        String info = String.format(
                "Exception while reconnection thread running" +
                        "\n" +
                        "Agent UID: {0}" +
                        "\n" +
                        "Agent status: {1}",
                agent.UID, agent.isConnected
        );
        eventSystem.ReconnectThreadException(info);
        if(!agent.isConnected) AgentDisconnected(agent, true);
    }

    public void ReceiverProvideBytes(ArrayList<Byte> bytes)
    {
        eventSystem.BytesRecieved(bytes);
        //Provide some more info
    }
}
