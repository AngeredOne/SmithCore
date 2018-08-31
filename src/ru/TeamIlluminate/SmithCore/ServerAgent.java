package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.net.Socket;
import ru.TeamIlluminate.SmithCore.StateManager.codes;

 class ServerAgent {
    private Socket socket;
    public Socket getSocket() {return socket;}
    private NetworkStream stream;
    private SmithProtocol protocol;
    private String UID;
    public boolean isConnected;

    public ServerAgent (Socket socket, String UID) throws IOException {
        this.socket = socket;
        this.UID = UID;
        this.protocol = new SmithProtocol(new NetworkStream(socket.getInputStream(), socket.getOutputStream()));
    }

    public void initSend(Byte[] data)
    {
         codes code = protocol.send(data);
         if(code == codes.SendException)
         {
             //Call connectHandler
         }
    }

    public void initRecieve()
    {
        codes code = protocol.recieve();

        if(code == codes.ReceiveException)
        {
            //Call connectHandler
        }
        else if(code == codes.DissconectionFlag)
        {
            //Call delete agent
        }
    }


    private void agentDisconnected()
    {

    }

    private void connectHandler()
    {

    }

}
