package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SmithProtocol implements Protocol, AgentLeavedHandler {

    private Agent agent;
    private NetworkStream stream;
    private List<SmithPackage> packageList;
    private int errorPackage = -1;

    public SmithProtocol(NetworkStream _stream, Agent agent)
    {
        this.stream = _stream;
        this.agent = agent;
    }

    private List<SmithPackage> formPackages(Byte[] data)
    {
        List<SmithPackage> formedPackages = new ArrayList<SmithPackage>();

        int packCount = data.length / 60;
        int notFullData = data.length % 60;


        for(int i = 0; i < packCount; ++i)
        {
            SmithPackage pack = new SmithPackage();
            byte[] dataByte = new byte[60];

            for(int j = 0; j < 60; ++j)
            {
                dataByte[j] = data[ i * 60 + j];
            }
            pack.data = dataByte;
            formedPackages.add(pack);
        }


        if(notFullData > 0)
        {
            byte[] dataByte = new byte[60];
            SmithPackage pack = new SmithPackage();

            pack.flag.pSize = (byte)notFullData;

            for(int i = 0; i < notFullData; ++i)
            {
                dataByte[i] = data[packCount * 60 + i];
            }
            pack.data = dataByte;
            formedPackages.add(pack);
        }

        return formedPackages;
    }

    public void Send()
    {
        if(packageList.size() > 0) {
            int sendBegin = 0;
            if (errorPackage > -1) sendBegin = errorPackage;

            for (int i = sendBegin; i < packageList.size(); ++i) {
                try {
                    stream.output.write(packageList.get(i).getBytes());
                    stream.output.flush();
                } catch (IOException e) {
                    errorPackage = i;
                    StateManager.instance().CommunicationException(e.getMessage(), this, i, agent);
                }
            }
            packageList.clear();
        }
    }

    @Override
    public void SendInit(Byte[] bytes) {

        packageList = formPackages((bytes));
        packageList.get(packageList.size() - 1).flag.EndTransmission = true;

        Send();
    }

    @Override
    public void Receive() {
        new Reciever(agent, this, stream).start();
    }

    @Override
    public void AgentLeaved(Agent agent) {
        Send();
    }
}

class Reciever extends Thread
{
    Agent agent;
    Protocol protocol;
    NetworkStream stream;
    ArrayList<SmithPackage> receivedPackages = new ArrayList<>();

    @Override
    public void run()
    {
        Receive();
    }

    public Reciever(Agent agent, Protocol protocol, NetworkStream stream)
    {
        this.agent = agent;
        this.protocol = protocol;
        this.stream = stream;
    }

    void Receive()
    {

        ArrayList<SmithPackage> recPackages = new ArrayList<>();

        ArrayList<Byte> recivedBytes = new ArrayList<>();

        boolean isEndedTransmission = false;
        boolean isClientDisconneted = false;

        while (!isEndedTransmission & isClientDisconneted)
        {
            byte[] buffer = new byte[64];
            try {
                stream.input.read(buffer);
            } catch (IOException e) {
                StateManager.instance().CommunicationException(e.getMessage(), protocol, recPackages.size(), agent);
                return;
            }

            FlagByte flags = new FlagByte().getFlags(Arrays.copyOfRange(buffer, 0, 3));
            byte[] data = Arrays.copyOfRange(buffer, 4, 63);


            if(flags.Disconnect)
            {
                isClientDisconneted = true;
                isEndedTransmission = true;
            }
            else if(flags.Resended)
            {
                recPackages = receivedPackages;
                //Это всё, потому что вставляем коллекцию наших уже полученных пакетов и, начиная с этого пакета продолжаем собирать эту коллекцию.
            }
            else if(flags.EndTransmission)
            {
                int bytesCount = flags.pSize;
                Byte[] lastBytes = new Byte[bytesCount];

                for(int i = 0; i < bytesCount; ++i)
                {
                    lastBytes[i] = buffer[i];
                }

                recivedBytes.addAll(Arrays.asList(lastBytes));
                isEndedTransmission = true;
                break;
            }

            recPackages.add(new SmithPackage(flags, data));

            Byte[] dataBytes = new Byte[60];

            //Say HELLO JAVA here, `cuz we need place some idiot code here 4r provide byte => Byte convertation (|-_-|)
            for(int i = 0; i < data.length; ++i)
            {
                dataBytes[i] = data[i];
            }

            recivedBytes.addAll(Arrays.asList(dataBytes));

            if(recivedBytes.size() > 0)
                StateManager.instance().ReceiverProvideBytes(recivedBytes);
            receivedPackages.clear();
        }
        currentThread().interrupt();
    }
}