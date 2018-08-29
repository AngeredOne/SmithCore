package ru.TeamIlluminate.SmithCore;

import ru.TeamIlluminate.SmithCore.StateManager.codes;

import java.net.Socket;
import java.util.List;

 class ServerCore {
    private Socket socket;
    private List<ServerAgent> agentList;
    private Validator validator;

    public ServerCore (int port, int timeout) {


    }

    public codes init() {

       return null;
    }

    public codes start() {

      return null;
    }

    public void stop() {

    }
    public void dropAgent() {

    }


    public ServerAgent getAgent(Socket client) {
      return null;
    }

    private void listen() {

    }

}