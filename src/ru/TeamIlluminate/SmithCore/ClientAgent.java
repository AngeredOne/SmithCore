package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

class ClientAgent extends Agent implements CoreEventHandler.AgentLeavedHandler, CoreEventHandler.AgentReconnectHandler {

    private Socket socket;
    private SocketAddress serverAddress;
     ClientAgent() {
        super("");
        StateManager.instance().subcribeHandler(this);
        socket = new Socket();
    }

     void connect(SocketAddress serverAddress) {

        this.serverAddress = serverAddress;

        try {
            socket.connect(serverAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
     public void AgentLeaved(Agent agent) {
        new ReconnectSystem(this).start();
    }

    @Override
    public void AgentReconnected(Agent agent) {
        isConnected = true;
    }

    class ReconnectSystem extends Thread
    {

        private ClientAgent agent = null;
        private int timeOut = 0;

         ReconnectSystem(ClientAgent agent)
        {
            this.agent = agent;
            timeOut = 0;
        }


        @Override
        public void run()
        {
            connectHandler();
        }

        private void connectHandler()
        {
            while(!agent.isConnected) {
                if (timeOut < APIServer.getTimeout() * 5) // timeOut = 5 equals 1 second
                {
                    timeOut++;
                    try {
                        agent.socket.connect(agent.serverAddress);
                        Thread.currentThread().sleep(200);
                    } catch (Exception e) {
                        StateManager.instance().ReconnectionThreadException(e.getMessage(), agent);
                        break;
                    }
                }
                else
                {
                    StateManager.instance().AgentDisconnected(agent, true);
                    break;
                }
            }
            if(agent.isConnected) StateManager.instance().AgentReconnected(agent);
            currentThread().interrupt();
        }
    }

}