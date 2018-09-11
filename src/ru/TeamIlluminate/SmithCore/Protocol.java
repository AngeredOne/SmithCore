package ru.TeamIlluminate.SmithCore;

import ru.TeamIlluminate.SmithCore.StateManager.RETURN_CODE;

public interface Protocol {
    public void SendInit(Byte[] bytes);
    public void Receive();
}
