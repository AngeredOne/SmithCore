package ru.TeamIlluminate.SmithCore;

import ru.TeamIlluminate.SmithCore.StateManager.codes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

 class ServerCore {
    private ServerSocket serverSocket;
    private List<ServerAgent> agentList;
    private Validator validator;
    private int timeout;
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
       this.timeout = timeout;
       this.validator = new Validator();

    }

     //needcheckportopening
     //internetconnection
    public codes start() {
        if (!isStarted) {
            listener = new ClientsListener();
            listener.run();
        }
        return null;
    }

    public void stop() {
        if (isStarted) {
            listener.isActive = false;
        }
    }

    public void dropAgent(Socket required) {
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
    }

    public ServerAgent getAgent(Socket required) {
      for (ServerAgent concreteAgent : agentList) {
         Socket concreteSocket = concreteAgent.getSocket();
         boolean isSocketEquals =
                 concreteSocket.getPort() ==
                 required.getPort()
                 &&
                 concreteSocket.getInetAddress().getCanonicalHostName() ==
                         required.getInetAddress().getCanonicalHostName();
         if (isSocketEquals)
            return concreteAgent;
      }
      return null;
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