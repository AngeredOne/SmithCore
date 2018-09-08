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
        ReceiveOK
    }
}
