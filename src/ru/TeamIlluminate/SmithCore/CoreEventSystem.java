package ru.TeamIlluminate.SmithCore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;
import ru.TeamIlluminate.SmithCore.CoreEventHandler.*;

class CoreEventSystem {
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

     void subscribe(CoreEventHandler sub) {
        for (Class<? extends CoreEventHandler> classType : subscribers.keySet()) {
            if (classType.isAssignableFrom(sub.getClass())) {
                subscribers.get(classType).add(sub);
            }
        }
    }

    void AgentConnected(Agent agent) {
    }

    @EventMethod(typeEvent = AgentDisconnectedHandler.class)
    void AgentDisconnected(Agent agent) {
        for (CoreEventHandler handler : subscribers.get(AgentDisconnectedHandler.class))
            ((AgentDisconnectedHandler) handler).AgentDisconnected(agent);
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

    @EventMethod(typeEvent = BytesRecievedHandler.class)
    void BytesRecieved(ArrayList<Byte> bytes) {
        for (CoreEventHandler handler : subscribers.get(BytesRecievedHandler.class))
            ((BytesRecievedHandler) handler).BytesRecived(bytes);
    }

    @EventMethod(typeEvent = AgentLeavedHandler.class)
    void AgentLeave(Agent agent) {
        for (CoreEventHandler handler : subscribers.get(AgentLeavedHandler.class))
            ((AgentLeavedHandler) handler).AgentLeaved(agent);
    }

    @EventMethod(typeEvent = CommunicationExceptionHandler.class)
    void CommunicationException(String message) {
        for (CoreEventHandler handler : subscribers.get(CommunicationExceptionHandler.class))
            ((CommunicationExceptionHandler) handler).CommunicationException(message);
    }

    @EventMethod(typeEvent = ReconnectThreadExceptionHandler.class)
    void ReconnectThreadException(String message) {
        for (CoreEventHandler handler : subscribers.get(ReconnectThreadExceptionHandler.class))
            ((ReconnectThreadExceptionHandler) handler).ReconnectThreadException(message);
    }

}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface EventMethod{
    Class<? extends CoreEventHandler> typeEvent() default CoreEventHandler.class;
}