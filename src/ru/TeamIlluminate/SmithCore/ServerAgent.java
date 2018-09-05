package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;

import ru.TeamIlluminate.SmithCore.StateManager.codes;

class ServerAgent extends Agent {

    private ReconnectSystem rSys;

    public boolean isConnected;

    public ServerAgent(Socket socket, String UID) throws IOException {
        super(socket, UID);

        protocol = new SmithProtocol(new NetworkStream(socket.getInputStream(), socket.getOutputStream()));
        this.rSys = new ReconnectSystem(this);
    }

    public void initSend(Byte[] data) {
        codes code = protocol.send(data);
        if (code == codes.SendException) {
            SM.AgentDisconnected(this, false);
            rSys.start();
        }
    }

    public void initRecieve() {
        codes code = protocol.recieve();

        if (code == codes.ReceiveException) {
            SM.AgentDisconnected(this, false);
            rSys.start();
        } else if (code == codes.DissconectionFlag) {
            SM.AgentDisconnected(this, true);
        }
    }


    private void agentReconnected() {

    }

}

class ReconnectSystem extends Thread
{

    private ServerAgent agent = null;
    private StateManager SM;
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
                SM.AgentDisconnected(agent, true);
            }
        }
        //if agent reconnected, code will come here
        SM.AgentReconnected(agent);
    }

}
