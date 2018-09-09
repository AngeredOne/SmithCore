package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

class ClientAgent extends Agent implements AgentDisconnectedHandler, AgentReconnectHandler {

    private Socket socket;
    private SocketAddress serverAddress;
    public ClientAgent() {
        super("");
        StateManager.instance().eventSystem.subscribe(this);
        socket = new Socket();
    }

    public void connect(SocketAddress serverAddress) {

        this.serverAddress = serverAddress;

        try {
            socket.connect(serverAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void AgentDisconnected(Agent agent, boolean isFullDisconnected) {
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

        public ReconnectSystem(ClientAgent agent)
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
                if (timeOut < APIServer.max_Timeout * 5) // timeOut = 5 equals 1 second
                {
                    timeOut++;
                    try {
                        agent.socket.connect(agent.serverAddress);
                        Thread.currentThread().sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Call SM thread error(thread is aborted but code is run)
                    }
                }
                else
                {
                    StateManager.instance().eventSystem.AgentDisconnected(agent, true);
                }
            }
            //if agent reconnected, code will come here
            StateManager.instance().eventSystem.AgentReconnected(agent);
        }
    }

}