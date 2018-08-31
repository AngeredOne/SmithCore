package ru.TeamIlluminate.SmithCore;

public class StateManager {
    enum codes {
        SendException, // Exception while sending
        ReceiveException, //Exception while receiving
        DissconectionFlag, //Socket sended package with a disconnection flag
        SendOK, //Protocol send function end sending
        ReceiveOK //Protocol receive function end receiving
    }
}
