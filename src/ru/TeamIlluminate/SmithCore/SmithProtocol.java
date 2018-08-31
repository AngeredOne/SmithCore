package ru.TeamIlluminate.SmithCore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.TeamIlluminate.SmithCore.StateManager.codes;

class SmithProtocol {
    private NetworkStream stream;
    private List<SmithPackage> packageList;
    private List<SmithPackage> recivedPackages;
    private int errorPackage = -1;

    public SmithProtocol(NetworkStream _stream)
    {
        this.stream = _stream;
    }

    public codes send (Byte[] data)
    {
        packageList = formPackages((data));
        packageList.get(packageList.size() - 1).flag.EndTransmission = true;

        for(int i = 0; i < packageList.size(); ++i)
        {
            try {

                stream.output.write(packageList.get(i).getBytes());
                stream.output.flush();
            } catch (IOException e) {
                errorPackage = i;
                e.printStackTrace();
                return codes.SendException;
            }
        }
        return codes.SendOK;
    }

    public void resend() {

        packageList.get(errorPackage).flag.Resended = true;

        for(int i = errorPackage; i < packageList.size(); ++i)
        {
            try {
                stream.output.write(packageList.get(i).getBytes());
                stream.output.flush();
            } catch (IOException e) {
                errorPackage = i;
                e.printStackTrace();

            }
        }

    }

    public codes recieve () {

        List<Byte> recivedBytes = new ArrayList<>();

        boolean isEndedTransmission = false;

        while (!isEndedTransmission)
        {
            byte[] buffer = new byte[64];
            try {
                stream.input.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                return codes.ReceiveException;
            }

            FlagByte flags = new FlagByte().getFlags(Arrays.copyOfRange(buffer, 0, 3));

            if(flags.Disconnect)
            {
                //Call StateManager's event of receiving last package
                return codes.DissconectionFlag;
            }
            else if(flags.Resended)
            {
                //Call something 2 provide receiving process
                //Обработка рисендед, тоже ивент. Формально, на уровне протокола нужно ловить эту шнягу. То есть на уровне протокола сохранять состояние ПОЛУЧЕННЫХ пакеджиков)0)
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

            Byte[] dataBytes = new Byte[60];
            for(int i = 4; i < buffer.length; ++i)
            {
                dataBytes[i-4] = buffer[i];
            }
            recivedBytes.addAll(Arrays.asList(dataBytes));
        }
        //Call StateManager's event of receiving data
        return codes.ReceiveOK;
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
            for(int i = packCount * 60 + notFullData; i < data.length; ++i)
            {
                dataByte[i - packCount * 60] = 0;
            }
            pack.data = dataByte;
            formedPackages.add(pack);
        }

        return formedPackages;
    }

    private byte[] parsePackages() {

        return null;
    }

}
