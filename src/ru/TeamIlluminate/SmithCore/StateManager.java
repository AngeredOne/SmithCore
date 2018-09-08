package ru.TeamIlluminate.SmithCore;

class StateManager {

    private static StateManager stateManager;
    public CoreEventSystem eventSystem;

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
        ReceiveOK,
        // Host has launched without errors and now waiting for connections
        HostStarted,
        // Host.start returned expection
        HostStartedException,
        // Host has dropped all agents and stopped accepting new agents
        HostStopped,
        // Host.stop returned exception
        HostStoppedException,
        // Host has dropped the agent successfully
        HostDroppedAgent,
        // Host already hasn't this agent in list
        HostDroppedAgentException_NoAgentInList

    }
}
