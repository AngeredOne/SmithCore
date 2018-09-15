package ru.TeamIlluminate.SmithCore;

 class APIClient {

  public void subcribeHandler(CoreEventHandler handler) {
     StateManager.instance().subcribeHandler(handler);
  }
}
