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
       } catch (IOException e) {
           e.printStackTrace();
       }
       this.validator = new Validator();
    }

    public RETURN_CODE start() {
        if (!isStarted) {
            isStarted = true;
            listener = new ClientsListener();
            listener.run();
            return RETURN_CODE.HostStarted;
        }
        return RETURN_CODE.HostStartedException;
    }

    public RETURN_CODE stop() {
        if (isStarted) {
            agentList.clear();
            isStarted = false;
            listener.isActive = false;
            return RETURN_CODE.HostStopped;
        }
        return RETURN_CODE.HostStoppedException;
    }

    public RETURN_CODE dropAgent(Socket required) {
        boolean result = agentList.removeIf(agent -> agent.getSocket() == required);
        if (result)
            return RETURN_CODE.HostDroppedAgent;
        else return RETURN_CODE.HostDroppedAgentException_NoAgentInList;
    }

    public ServerAgent getAgent(Socket required) {
        return agentList.stream().filter(agent -> agent.getSocket() == required).findFirst().get();
    }


    class ClientsListener extends Thread {
        public boolean isActive = true;
        @Override
        public void run() {
            while (isActive) {
                try {
                    Socket inSocket = serverSocket.accept();
                    String UID = validator.getUID(inSocket);
                    ServerAgent newAgent = new ServerAgent(inSocket, UID);
                    agentList.add(newAgent);
                    StateManager.instance().eventSystem.HostAcceptedNewAgent(newAgent);
                } catch (SocketTimeoutException e) {
                    StateManager.instance().eventSystem.HostConnectionTimeout();
                } catch (IOException e) {
                    StateManager.instance().eventSystem.HostAcceptingConnectionError();
                }
            }
        }


    }

}