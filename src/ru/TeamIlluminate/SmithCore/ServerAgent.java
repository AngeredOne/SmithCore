package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;

import ru.TeamIlluminate.SmithCore.StateManager.codes;

class ServerAgent {
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    private NetworkStream stream;
    private SmithProtocol protocol;
    private String UID;
    private ReconnectSystem rSys;

    public boolean isConnected;

    public ServerAgent(Socket socket, String UID) throws IOException {
        this.socket = socket;
        this.UID = UID;
        this.protocol = new SmithProtocol(new NetworkStream(socket.getInputStream(), socket.getOutputStream()));
        this.rSys = new ReconnectSystem(this);
    }

    public void initSend(Byte[] data) {
        codes code = protocol.send(data);
        if (code == codes.SendException) {
            rSys.start();
        }
    }

    public void initRecieve() {
        codes code = protocol.recieve();

        if (code == codes.ReceiveException) {
            rSys.start();
        } else if (code == codes.DissconectionFlag) {
            //Call delete agent
        }
    }


    private void agentDisconnected() {

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
                //Call StateManager, agent full disconnected
            }
        }
        //if agent reconnected, code will come here
        //Call StateManager, agent reconnected
    }

}
