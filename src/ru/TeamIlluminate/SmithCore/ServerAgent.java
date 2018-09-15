package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;

class ServerAgent extends Agent {

    private ReconnectSystem rSys;
    private Socket socket;

     ServerAgent(String UID, Socket socket){
        super(UID);

        this.socket = socket;

        try {
            protocol = new SmithProtocol(new NetworkStream(socket.getInputStream(), socket.getOutputStream()), this);
        } catch (IOException ex) {}
    }

      void AgentDisconnected()
     {
         new ReconnectSystem(this).start();
     }


     Socket getAgentSocket()
    {
        return this.socket;
    }

    class ReconnectSystem extends Thread
    {

        private ServerAgent agent = null;
        private int timeOut = 0;

         ReconnectSystem(ServerAgent agent)
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
                        Thread.currentThread().sleep(200);
                    } catch (InterruptedException e) {
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
            if(agent.isConnected)
            StateManager.instance().AgentReconnected(agent);
            currentThread().interrupt();
        }
    }

}

