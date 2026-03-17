package com.dmoser.codyssey.bifroest.session;

import com.dmoser.codyssey.bifroest.io.Communication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session {

  private static final ThreadLocal<Session> SESSION = new ThreadLocal<>();

  private final String name;
  private final Communication io;
  List<String> currentPath = new ArrayList<>();
  Map<String, Object> variables = new HashMap<>();
  private boolean isRunning = true;

  private Session(String name, Communication communication) {
    this.name = name;
    this.io = communication;
  }

  public static SetNameStep create() {
    Session currentSession = SESSION.get();
    if (currentSession != null) {
      throw new IllegalStateException("Session should not be created twice");
    }
    return new SessionBuilder();
  }

  public static Session get() {
    Session currentSession = SESSION.get();
    if (currentSession == null) {
      throw new IllegalStateException("Session is not created");
    }
    return SESSION.get();
  }

  public static void close() {
    get().getIO().close();
    SESSION.remove();
  }

  public static Communication io() {
    return get().getIO();
  }

  public Communication getIO() {
    return io;
  }

  public String getName() {
    return name;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void stop() {
    this.isRunning = false;
  }

  public List<String> getCurrentPath() {
    return currentPath;
  }

  public void setCurrentPath(List<String> newPath) {
    this.currentPath = newPath;
  }

  public void putVariable(String key, Object value) {
    variables.put(key, value);
  }

  public Object getVariable(String key) {
    return variables.get(key);
  }

  public interface SetNameStep {
    SetCommunication withName(String name);
  }

  public interface SetCommunication {
    SetOptionalsStep andCommunication(Communication communication);
  }

  public interface SetOptionalsStep {
    Session open();
  }

  public static class SessionBuilder implements SetNameStep, SetCommunication, SetOptionalsStep {
    String name;
    Communication communication;

    @Override
    public SetOptionalsStep andCommunication(Communication communication) {
      this.communication = communication;
      return this;
    }

    @Override
    public SetCommunication withName(String name) {
      this.name = name;
      return this;
    }

    @Override
    public Session open() {
      Session newSession = new Session(name, communication);
      SESSION.set(newSession);
      return Session.get();
    }
  }
}
