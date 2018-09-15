package ru.TeamIlluminate.SmithCore;

import java.net.Socket;

public class APIServer {

    private static int max_Timeout = 180;
    Host host = new Host();

    public static int getTimeout()
    {
        return max_Timeout;
    }

    public void StartHost(int port, int max_Timeout, boolean SafeTransaction)
    {
        this.max_Timeout = max_Timeout;
        if(SafeTransaction) {
            host.start(port);
        }
    }

    public void StopHost(boolean SafeTransaction)
    {
        if(SafeTransaction)
            host.stop();
    }

    public void subcribeHandler(CoreEventHandler handler) {
        StateManager.instance().subcribeHandler(handler);
    }

    public void Send(Socket client, Byte[] bytes)
    {
        host.getAgent(client).protocol.SendInit(bytes);
    }

    public void Kick(Socket socket)
    {
        if(host != null) host.dropAgent(socket);
        else; //Вывести ивент что дев - гей, и вообще так не поступают
    }

}
