package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import ru.TeamIlluminate.SmithCore.StateManager.RETURN_CODE;

class HostAgent {
    private ServerSocket serverSocket;
    private ArrayList<ServerAgent> agentList = new ArrayList<>();
    private Validator validator;
    private ClientsListener listener;
    private boolean isStarted = false;

    public HostAgent(int port, int timeout) {
       try {
           serverSocket = new ServerSocket(port);
           serverSocket.setSoTimeout(timeout);
           validator = new Validator();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public boolean start() {
        if (!isStarted) {
            isStarted = true;
            listener = new ClientsListener();
            listener.run();
            return true;
        }
        return false;
    }

    public boolean stop() {
        if (isStarted) {
            agentList.clear();
            isStarted = false;
            listener.isActive = false;
            return true;
        }
        return false;
    }

    public boolean dropAgent(Socket required) {
        boolean result = agentList.removeIf(agent -> agent.getAgentSocket() == required);
        if (result)
            return true;
        else return false;
    }

    public ServerAgent getAgent(Socket required) {
        return agentList.stream().filter(agent -> agent.getAgentSocket() == required).findFirst().get();
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
                    StateManager.instance().eventSystem.HostAcceptingConnectionError();
                }
            }
        }
    }
}