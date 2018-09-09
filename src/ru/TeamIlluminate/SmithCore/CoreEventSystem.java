package ru.TeamIlluminate.SmithCore;

import com.sun.security.ntlm.Server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;

public class CoreEventSystem {
    private HashMap<Class<? extends CoreEventHandler>, List<CoreEventHandler>> subscribers = new HashMap<>();

    CoreEventSystem() {
        ArrayList<EventMethod> eventMethods = new ArrayList<>();
        for (Method method : getClass().getMethods()) {
            EventMethod concreteMethod = method.getAnnotation(EventMethod.class);
            if (concreteMethod instanceof EventMethod)
                eventMethods.add(concreteMethod);
        }
        for (EventMethod method : eventMethods) {
            subscribers.put(method.typeEvent(), new ArrayList<>());
        }
    }

    public void subscribe(CoreEventHandler sub) {
        for (Class<? extends CoreEventHandler> classType : subscribers.keySet()) {
            if (sub.getClass().equals(classType)) {
                subscribers.get(classType).add(sub);
            }
        }
    }

    @EventMethod(typeEvent = AgentDisconnectedHandler.class)
    void AgentDisconnected(Agent agent, boolean isFullDisconnected) {
        for (CoreEventHandler handler : subscribers.get(AgentDisconnectedHandler.class))
            ((AgentDisconnectedHandler) handler).AgentDisconnected(agent, isFullDisconnected);
    }

    @EventMethod(typeEvent = AgentReconnectHandler.class)
    void AgentReconnected(Agent agent) {
        for (CoreEventHandler handler : subscribers.get(AgentReconnectHandler.class))
            ((AgentReconnectHandler) handler).AgentReconnected(agent);
    }

    @EventMethod(typeEvent = HostConnectioTimeoutHandler.class)
    void HostConnectionTimeout() {
        for (CoreEventHandler handler : subscribers.get(HostConnectioTimeoutHandler.class))
            ((HostConnectioTimeoutHandler) handler).HostConnectionTimeout();
    }

    @EventMethod(typeEvent = HostAcceptedNewAgent.class)
    void HostAcceptedNewAgent(ServerAgent agent) {
        for (CoreEventHandler handler : subscribers.get(HostAcceptedNewAgent.class))
            ((HostAcceptedNewAgent) handler).HostAcceptedNewAgent(agent);
    }

    @EventMethod(typeEvent = HostAcceptedReconnect.class)
    void HostAcceptedReconnect(ServerAgent agent) {
        for (CoreEventHandler handler : subscribers.get(HostAcceptedReconnect.class))
            ((HostAcceptedReconnect) handler).HostAcceptedReconnect(agent);
    }

    @EventMethod(typeEvent = HostStartedHandler.class)
    void HostStarted() {
        for (CoreEventHandler handler : subscribers.get(HostStartedHandler.class))
            ((HostStartedHandler) handler).HostStarted();
    }

    @EventMethod(typeEvent = HostStoppedHandler.class)
    void HostStopped() {
        for (CoreEventHandler handler : subscribers.get(HostStoppedHandler.class))
            ((HostStoppedHandler) handler).HostStopped();
    }

    @EventMethod(typeEvent = BytesRecievedHandler.class)
    void BytesRecieved(ArrayList<Byte> bytes) {
        for (CoreEventHandler handler : subscribers.get(BytesRecievedHandler.class))
            ((BytesRecievedHandler) handler).BytesRecived(bytes);
    }


}
@Target(ElementType.METHOD)
@interface EventMethod{
    Class<? extends CoreEventHandler> typeEvent() default CoreEventHandler.class;
}

interface CoreEventHandler {}
interface AgentDisconnectedHandler extends CoreEventHandler { void AgentDisconnected(Agent agent, boolean isFullDisconnected); }
interface AgentReconnectHandler extends CoreEventHandler { void AgentReconnected(Agent agent); }
interface BytesRecievedHandler extends CoreEventHandler { void BytesRecived(ArrayList<Byte> bytes); }
interface ReconnectThreadIsAbortedHandler extends CoreEventHandler  { void ReconnectThreadIsAborted(); }
interface HostConnectioTimeoutHandler extends CoreEventHandler { void HostConnectionTimeout(); }
interface HostAcceptedNewAgent extends CoreEventHandler { void HostAcceptedNewAgent(ServerAgent agent); }
interface HostStartedHandler extends CoreEventHandler { void HostStarted(); }
interface HostStoppedHandler extends CoreEventHandler { void HostStopped(); }
interface HostAcceptedReconnect extends CoreEventHandler { void HostAcceptedReconnect(ServerAgent agent); }