package ru.TeamIlluminate.SmithCore;

import ru.TeamIlluminate.SmithCore.StateManager.codes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

 class ServerCore {
    private Socket socket;
    private List<ServerAgent> agentList;
    private Validator validator = new Validator();
    private int timeout;

    public ServerCore (int port, int timeout) {
       this.timeout = timeout;
       this.validator = new Validator();
       try {
          Socket socket = new Socket(InetAddress.getLocalHost(), port);
       } catch (IOException e) {
           e.printStackTrace();
       }

    }

    public codes start() {

      return null;
    }

    public void stop() {

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

    private void listen() {

    }

}