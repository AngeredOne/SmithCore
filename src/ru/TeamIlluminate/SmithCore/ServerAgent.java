package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;

import ru.TeamIlluminate.SmithCore.StateManager.RETURN_CODE;

import static ru.TeamIlluminate.SmithCore.StateManager.stateManager;

class ServerAgent extends Agent {

    private ReconnectSystem rSys;
    private Socket socket;

    public boolean isConnected;

    public ServerAgent(String UID, Socket socket){
        super(UID);

        this.socket = socket;

        try {
            protocol = new SmithProtocol(new NetworkStream(socket.getInputStream(), socket.getOutputStream()));
        } catch (IOException ex) {}

        this.rSys = new ReconnectSystem(this);
    }

    public Socket getAgentSocket()
    {
        return this.socket;
    }

}

class ReconnectSystem extends Thread
{

    private ServerAgent agent = null;
    private int timeOut = 0;

    public ReconnectSystem(ServerAgent agent)
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
                    Thread.currentThread().sleep(200);
                } catch (InterruptedException e) {
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
