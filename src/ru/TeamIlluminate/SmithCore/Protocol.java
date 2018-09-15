package ru.TeamIlluminate.SmithCore;

import ru.TeamIlluminate.SmithCore.StateManager.RETURN_CODE;

 interface Protocol {
     void SendInit(Byte[] bytes);
     void Receive();
}
