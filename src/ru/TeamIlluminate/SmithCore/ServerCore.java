package ru.TeamIlluminate.SmithCore;

import java.net.Socket;
import java.util.List;

 class ServerCore {
    private Socket socket;
    private List<ServerAgent> agentList;
    private Validator validator;

    public ServerCore (int port, int timeout) {}
    public void init() {}
    public void start() {}
    public void stop() {}
    public void dropAgent() {}
    public ServerAgent getAgent(Socket client) { return null; }

    private void listen() {}

}