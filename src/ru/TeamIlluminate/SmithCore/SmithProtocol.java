package ru.TeamIlluminate.SmithCore;

import com.sun.xml.internal.ws.commons.xmlutil.Converter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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

    public void send (Byte[] data)
    {
        packageList = formPackages((data));
        packageList.get(packageList.size() - 1).flag.EndTransmission = true;

        for(int i = 0; i < packageList.size(); ++i)
        {
            try {

                stream.output.write(packageList.get(i).getBytes());
                stream.output.flush();
            } catch (IOException e) {
                //Не ну тут короче ивент хуё-моё
                errorPackage = i;
                e.printStackTrace();
            }
        }
    }

    public void resend() {

        packageList.get(errorPackage).flag.Resended = true;

        for(int i = errorPackage; i < packageList.size(); ++i)
        {
            try {
                stream.output.write(packageList.get(i).getBytes());
                stream.output.flush();
            } catch (IOException e) {
                //Не ну тут короче ивент хуё-моё
                errorPackage = i;
                e.printStackTrace();
            }
        }

    }

    public codes recieve () {

        List<SmithPackage> packeges = new ArrayList<SmithPackage>();
        SmithPackage pack = new SmithPackage();

        boolean isEndedTransmission = false;

        while (!isEndedTransmission)
        {
            byte[] buffer = new byte[64];
            try {
                stream.input.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                //Здесь ивент, обрыв связи при чтении пакетов из потока.
            }
            byte[] flagsByte = new byte[4];

            for(int i = 0; i < 4; ++i)
            {
                flagsByte[i] = buffer[i];
            }

            FlagByte flags = new FlagByte().getFlags(flagsByte);

            if(flags.Disconnect)
            {
                //Ивент дисконнекта
                break;
            }
            else if(flags.Resended)
            {
                //Обработка рисендед, тоже ивент. Формально, на уровне протокола нужно ловить эту шнягу. То есть на уровне протокола сохранять состояние ПОЛУЧЕННЫХ пакеджиков)0)
            }
            else if(flags.EndTransmission)
            {
                //тут ласт пакедж, вызывает досвидули на уровне метода))0))
                isEndedTransmission = true;
                break;
            }

            byte[] dataBytes = new byte[60];
            for(int i = 4; i < buffer.length; ++i)
            {
                dataBytes[i-4] = buffer[i];
            }
            pack.flag = flags;
            pack.data = dataBytes;
            packeges.add(pack);
        }

        return null;
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
