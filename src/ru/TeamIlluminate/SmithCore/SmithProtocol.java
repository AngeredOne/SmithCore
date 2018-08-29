package ru.TeamIlluminate.SmithCore;

import com.sun.xml.internal.ws.commons.xmlutil.Converter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class SmithProtocol {
    private NetworkStream stream;
    private List<SmithPackage> packageList;
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

    public byte[] recieve () {

        List<SmithPackage> packeges = new ArrayList<SmithPackage>();
        boolean isEndedTransmission = false;
        while (!isEndedTransmission)
        {
            byte[] buffer = new byte[64];
            stream.input.read(buffer);
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
            }
        }

        return new byte[]{};
    }

    private List<SmithPackage> formPackages(Byte[] data) {return null;}

    //Something "stream" instead for network stream
    private byte[] parsePackages() {

        return null;
    }

}
