package ru.TeamIlluminate.SmithCore;

import ru.TeamIlluminate.SmithCore.StateManager.RETURN_CODE;

public interface Protocol {

    public RETURN_CODE Send(Byte[] bytes);
    public RETURN_CODE Receive();
}
