package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import ru.TeamIlluminate.SmithCore.StateManager.RETURN_CODE;

class Host implements CoreEventHandler.AgentLeavedHandler {
    private ServerSocket serverSocket;
    private ArrayList<ServerAgent> agentList = new ArrayList<>();
    private Validator validator;
    private ClientsListener listener;
    private boolean isStarted = false;

    Host() {
        validator = new Validator();
        StateManager.instance().subcribeHandler(this);
    }

     void start(int port) {
         try {
             serverSocket = new ServerSocket(port);
             if (!isStarted) {
                 isStarted = true;
                 listener = new ClientsListener();
                 listener.run();
             }
             else
             {
                 //Уведомить о попытке запуска запущенного
             }
         } catch (IOException e) {
             e.printStackTrace(); //тут нужно сделать вывод ивента и ивент сам.
         }
    }

     void stop() {
        if (isStarted) {
            agentList.clear();
            isStarted = false;
            listener.interrupt();
        }
        else
        {
            //Ивент что останавливаем остановленное
        }
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
    public void AgentLeaved(Agent agent) {
        agentList.stream().filter(agentFromList -> agentFromList == agent).findFirst().get();
    }

    class ClientsListener extends Thread {
         boolean isActive = true;
        @Override
        public void run() {
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