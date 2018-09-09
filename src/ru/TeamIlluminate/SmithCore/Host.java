package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import ru.TeamIlluminate.SmithCore.StateManager.RETURN_CODE;

class Host implements AgentDisconnectedHandler {
    private ServerSocket serverSocket;
    private ArrayList<ServerAgent> agentList = new ArrayList<>();
    private Validator validator;
    private ClientsListener listener;
    private boolean isStarted = false;

    public Host(int port, int timeout) {
       try {
           serverSocket = new ServerSocket(port);
           serverSocket.setSoTimeout(timeout);
           validator = new Validator();
           StateManager.instance().eventSystem.subscribe(this);
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public boolean start() {
        if (!isStarted) {
            isStarted = true;
            listener = new ClientsListener();
            listener.run();
            StateManager.instance().eventSystem.HostStarted();
            return true;
        }
        return false;
    }

    public boolean stop() {
        if (isStarted) {
            agentList.clear();
            isStarted = false;
            listener.isActive = false;
            StateManager.instance().eventSystem.HostStopped();
            return true;
        }
        return false;
    }

    public boolean dropAgent(Socket required) {
        ServerAgent agentToRemove = agentList.stream().filter(agent -> agent.getAgentSocket() == required).findFirst().get();
        if (agentToRemove instanceof ServerAgent) {
            StateManager.instance().eventSystem.AgentDisconnected(agentToRemove, true);
            return true;
        } else return false;
    }

    public ServerAgent getAgent(Socket required) {
        return agentList.stream().filter(agent -> agent.getAgentSocket() == required).findFirst().get();
    }

    @Override
    public void AgentDisconnected(Agent agent, boolean isFullDisconnected) {
        ServerAgent serverAgent = (ServerAgent) agent;
        if (agentList.contains(serverAgent)) {
            if (isFullDisconnected)
                agentList.remove(serverAgent);
            else serverAgent.AgentDisconnected();
        }
    }

    class ClientsListener extends Thread {
        public boolean isActive = true;
        @Override
        public void run() {
            while (isActive) {
                try {
                    Socket inSocket = serverSocket.accept();
                    ServerAgent agent = getAgent(inSocket);
                    if (getAgent(inSocket) instanceof ServerAgent) {
                        agent.isConnected = true;
                        StateManager.instance().eventSystem.HostAcceptedReconnect(agent);
                    } else {
                        agent = new ServerAgent(validator.getUID(inSocket), inSocket);
                        agentList.add(agent);
                        StateManager.instance().eventSystem.HostAcceptedNewAgent(agent);
                    }
                } catch (SocketTimeoutException e) {
                    StateManager.instance().eventSystem.HostConnectionTimeout();
                } catch (IOException e) {
                    //?
                    e.printStackTrace();
                }
            }
        }
    }
}