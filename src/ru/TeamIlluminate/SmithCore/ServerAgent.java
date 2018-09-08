package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;

import ru.TeamIlluminate.SmithCore.StateManager.RETURN_CODE;

import static ru.TeamIlluminate.SmithCore.StateManager.stateManager;

class ServerAgent extends Agent {

    private ReconnectSystem rSys;

    public boolean isConnected;

    public ServerAgent(Socket socket, String UID) throws IOException {
        super(socket, UID);

        protocol = new SmithProtocol(new NetworkStream(socket.getInputStream(), socket.getOutputStream()));
        this.rSys = new ReconnectSystem(this);
    }

    public void initSend(Byte[] data) {
        RETURN_CODE code = protocol.Send(data);
        if (code == RETURN_CODE.SendException) {
            StateManager.instance().eventSystem.AgentDisconnected(this, false);
            rSys.start();
        }
    }

    public void initRecieve() {
        RETURN_CODE code = protocol.Receive();

        if (code == RETURN_CODE.ReceiveException) {
            StateManager.instance().eventSystem.AgentDisconnected(this, false);
            rSys.start();
        } else if (code == RETURN_CODE.DissconectionFlag) {
            StateManager.instance().eventSystem.AgentDisconnected(this, true);
        }
    }


    private void agentReconnected() {

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
