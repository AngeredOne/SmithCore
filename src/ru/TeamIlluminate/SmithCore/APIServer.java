package ru.TeamIlluminate.SmithCore;

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

}
