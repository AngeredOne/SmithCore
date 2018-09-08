package ru.TeamIlluminate.SmithCore;

import com.sun.security.ntlm.Client;

import java.util.*;

public class CoreEventSystem {
    private HashMap<EventCodes, List<CoreEventHandler>> subscribers = new HashMap<>();
    public enum EventCodes {
        AgentDisconnected {
            public Class<AgentDisconnectedHandler> getEnumClass() { return ru.TeamIlluminate.SmithCore.AgentDisconnectedHandler.class; }
        },
        ServerDisconnected {
            public Class<ServerDisconnectedHandler> getEnumClass() { return ru.TeamIlluminate.SmithCore.ServerDisconnectedHandler.class; }
        };
        public abstract <T extends CoreEventHandler> Class<T> getEnumClass();
    }

    CoreEventSystem() {
        for (EventCodes code : EventCodes.values()) {
            subscribers.put(code, new ArrayList<>());
        }
    }

     public void subscribe(CoreEventHandler subs) {
        for (EventCodes code : EventCodes.values()) {
            if (subs.getClass().equals(code.getEnumClass())) {
                subscribers.get(code).add(subs);
            }
        }
    }

    public void AgentDisconnected(Agent agent, boolean isFullDisconnected) {
        for (CoreEventHandler handler : subscribers.get(EventCodes.AgentDisconnected))
            ((AgentDisconnectedHandler) handler).AgentDisconnected(agent, isFullDisconnected);
    }

    public void AgentReconnected(Agent agent) {

    }
}
interface CoreEventHandler {}
interface AgentDisconnectedHandler extends CoreEventHandler { void AgentDisconnected(Agent agent, boolean isFullDisconnected); }
interface ServerDisconnectedHandler extends CoreEventHandler {void ServerDisconnected(); }
interface AgentReconnectHandler extends CoreEventHandler { void AgentReconnected(Agent agent); }
interface ServerReconnectedHandler extends CoreEventHandler {void ServerReconnected(); }
interface BytesRecievedHandler extends CoreEventHandler {void BytesRecived(Byte[] bytes); }
interface ReconnectThreadIsAbortedHandler extends CoreEventHandler  { void ReconnectThreadIsAborted(); }