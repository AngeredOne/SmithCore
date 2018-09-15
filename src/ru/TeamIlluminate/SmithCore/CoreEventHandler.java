package ru.TeamIlluminate.SmithCore;

import java.util.ArrayList;

public interface CoreEventHandler {
    interface AgentDisconnectedHandler extends CoreEventHandler { void AgentDisconnected(Agent agent); }
    interface AgentLeavedHandler extends CoreEventHandler { void AgentLeaved(Agent agent); }
    interface AgentReconnectHandler extends CoreEventHandler { void AgentReconnected(Agent agent); }
    interface BytesRecievedHandler extends CoreEventHandler { void BytesRecived(ArrayList<Byte> bytes); }
    interface CommunicationExceptionHandler extends CoreEventHandler { void CommunicationException(String message); }
    interface ReconnectThreadExceptionHandler extends CoreEventHandler  { void ReconnectThreadException(String message); }
    interface HostConnectioTimeoutHandler extends CoreEventHandler { void HostConnectionTimeout(); }
    interface HostAcceptedNewAgent extends CoreEventHandler { void HostAcceptedNewAgent(ServerAgent agent); }
    interface HostAcceptedReconnect extends CoreEventHandler { void HostAcceptedReconnect(ServerAgent agent); }
}