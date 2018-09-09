package ru.TeamIlluminate.SmithCore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;

public class CoreEventSystem {
    private HashMap<Class<? extends CoreEventHandler>, List<CoreEventHandler>> subscribers = new HashMap<>();
    //Надо затестить
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
    public void AgentDisconnected(Agent agent, boolean isFullDisconnected) {
        for (CoreEventHandler handler : subscribers.get(AgentDisconnectedHandler.class))
            ((AgentDisconnectedHandler) handler).AgentDisconnected(agent, isFullDisconnected);
    }

    @EventMethod(typeEvent = AgentReconnectHandler.class)
    public void AgentReconnected(Agent agent) {
        for (CoreEventHandler handler : subscribers.get(AgentReconnectHandler.class))
            ((AgentReconnectHandler) handler).AgentReconnected(agent);
    }

    @EventMethod(typeEvent = HostConnectioTimeoutHandler.class)
    public void HostConnectionTimeout() {

    }

    public void HostAcceptingConnectionError() {

    }

    public void HostAcceptedNewAgent(ServerAgent agent) {

    }

}
@Target(ElementType.METHOD)
@interface EventMethod{
    Class<? extends CoreEventHandler> typeEvent() default CoreEventHandler.class;
}

interface CoreEventHandler {}
interface AgentDisconnectedHandler extends CoreEventHandler { void AgentDisconnected(Agent agent, boolean isFullDisconnected); }
interface ServerDisconnectedHandler extends CoreEventHandler {void ServerDisconnected(); }
interface AgentReconnectHandler extends CoreEventHandler { void AgentReconnected(Agent agent); }
interface ServerReconnectedHandler extends CoreEventHandler { void ServerReconnected(); }
interface BytesRecievedHandler extends CoreEventHandler { void BytesRecived(Byte[] bytes); }
interface ReconnectThreadIsAbortedHandler extends CoreEventHandler  { void ReconnectThreadIsAborted(); }
interface HostConnectioTimeoutHandler extends CoreEventHandler { }