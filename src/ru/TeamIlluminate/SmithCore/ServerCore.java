package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

class ServerCore {
    private ServerSocket serverSocket;
    private ArrayList<ServerAgent> agentList = new ArrayList<>();
    private Validator validator;
    private ClientsListener listener;
    private boolean isStarted = false;

     //error when createSocket
    public ServerCore (int port, int timeout) {
       try {
           serverSocket = new ServerSocket(port);
           serverSocket.setSoTimeout(timeout);
       } catch (IOException e) {
           e.printStackTrace();
       }
       this.validator = new Validator();
    }

     //needcheckportopening
     //internetconnection
    public StateManager.RETURN_CODE start() {
        if (!isStarted) {
            isStarted = true;
            listener = new ClientsListener();
            listener.run();
        }
        return null;
    }

    public StateManager.RETURN_CODE stop() {
        if (isStarted) {
            isStarted = false;
            listener.isActive = false;
        }
        return null;
    }

    public StateManager.RETURN_CODE dropAgent(Socket required) {
       for (ServerAgent concreteAgent : agentList) {
          Socket concreteSocket = concreteAgent.getSocket();
          boolean isSocketEquals =
                  concreteSocket.getPort() ==
                          required.getPort()
                          &&
                          concreteSocket.getInetAddress().getCanonicalHostName() ==
                                  required.getInetAddress().getCanonicalHostName();
          if (isSocketEquals) {
             agentList.remove(concreteAgent);
          }
       }
       return null;
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
                    //newagentevent
                } catch (SocketTimeoutException e) {
                    //Someone try connect but timeout;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}