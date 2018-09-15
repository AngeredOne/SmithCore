package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import ru.TeamIlluminate.SmithCore.StateManager.RETURN_CODE;

class Host implements AgentLeavedHandler {
    private ServerSocket serverSocket;
    private ArrayList<ServerAgent> agentList = new ArrayList<>();
    private Validator validator;
    private ClientsListener listener;
    private boolean isStarted = false;

     Host(int port, int timeout) {
       try {
           serverSocket = new ServerSocket(port);
           serverSocket.setSoTimeout(timeout);
           validator = new Validator();
           StateManager.instance().subcribeHandler(this);
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

     boolean start() {
        if (!isStarted) {
            isStarted = true;
            listener = new ClientsListener();
            listener.run();
            return true;
        }
        return false;
    }

     boolean stop() {
        if (isStarted) {
            agentList.clear();
            isStarted = false;
            listener.isActive = false;
            return true;
        }
        return false;
    }

     boolean dropAgent(Socket required) {
        ServerAgent agentToRemove = agentList.stream().filter(agent -> agent.getAgentSocket() == required).findFirst().get();
        if (agentToRemove instanceof ServerAgent) {
            StateManager.instance().AgentDisconnected(agentToRemove, true);
            return true;
        } else return false;
    }

     ServerAgent getAgent(Socket required) {
        return agentList.stream().filter(agent -> agent.getAgentSocket() == required).findFirst().get();
    }

    @Override
     void AgentLeaved(Agent agent) {
        agentList.stream().filter(agentFromList -> agentFromList == agent).findFirst().get();
    }

    class ClientsListener extends Thread {
         boolean isActive = true;
        @Override
         void run() {
            while (isActive) {
                try {
                    Socket inSocket = serverSocket.accept();
                    ServerAgent agent = getAgent(inSocket);
                    if (getAgent(inSocket) instanceof ServerAgent) {
                        agent.isConnected = true;
                        StateManager.instance().AgentReconnected(agent);
                    } else {
                        agent = new ServerAgent(validator.getUID(inSocket), inSocket);
                        agentList.add(agent);
                        StateManager.instance().AgentConnected(agent);
                    }
                } catch (SocketTimeoutException e) {
                    //??
                } catch (IOException e) {
                    //?
                    e.printStackTrace();
                }
            }
        }
    }
}