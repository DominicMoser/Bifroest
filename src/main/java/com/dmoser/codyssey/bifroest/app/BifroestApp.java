package com.dmoser.codyssey.bifroest.app;

import static com.dmoser.codyssey.bifroest.session.Session.get;

import com.dmoser.codyssey.bifroest.capabilities.*;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Response;
import com.dmoser.codyssey.bifroest.io.Result;
import com.dmoser.codyssey.bifroest.io.errors.ErrorCode;
import com.dmoser.codyssey.bifroest.io.flags.*;
import com.dmoser.codyssey.bifroest.session.Session;
import com.dmoser.codyssey.bifroest.structure.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class BifroestApp {

  protected Layer rootLayer;
  protected Map<Class<? extends Flag>, Capability> capabilityMap = new HashMap<>();
  protected Capability defaultFlagHandler = new UnhandledFlagHandler();
  protected Map<Pattern, Command> globalCommands = new HashMap<>();
  protected Prompt prompt;

  public BifroestApp(Layer rootLayer, Prompt prompt) {
    this.rootLayer = rootLayer;
    this.prompt = prompt;
  }

  public static SelectAppType builder() {
    return new SelectAppType();
  }

  protected abstract void loadCapabilities();

  protected abstract void loadGlobalCommands();

  protected Layer resolveActiveLayer() {
    List<String> currentPath = new ArrayList<>(get().getCurrentPath());
    Layer currentLayer = rootLayer;

    while (!currentPath.isEmpty() && currentLayer.hasLayer(currentPath.getFirst())) {
      currentLayer = currentLayer.getLayer(currentPath.getFirst());
      currentPath.removeFirst();
    }
    return currentLayer;
  }

  protected Result handleRequest(Request request) {

    Layer currentLayer = resolveActiveLayer();
    List<String> pathSegments = new ArrayList<>(get().getCurrentPath());

    while (currentLayer.hasLayer(request.getCommand())) {
      pathSegments.add(request.getCommand());
      currentLayer = currentLayer.getLayer(request.getCommand());
      if (!currentLayer.isAccessible(request.getCommand())) {
        return new CommandNotFoundFlag(ErrorCode.COMMAND_NOT_FOUND, "");
      }
      if (!request.movePointer()) {
        return Flags.navigationFlag(pathSegments);
      }
    }

    if (currentLayer.hasCommand(request.getCommand())) {
      return currentLayer.getCommand(request.getCommand()).execute(request);
    }

    request.resetPointer();

    for (Map.Entry<Pattern, Command> entry : globalCommands.entrySet()) {
      if (entry.getKey().matcher(request.getCommand()).matches()) {
        return entry.getValue().execute(request);
      }
    }
    return new CommandNotFoundFlag(ErrorCode.COMMAND_NOT_FOUND, request.getCommand());
  }

  public void run() {
    init();
    loadCapabilities();
    loadGlobalCommands();
    initSession();
    start();
    loop();
    stop();
  }

  public abstract void setCompleter();

  protected void init() {}

  protected abstract void initSession();

  protected abstract void start();

  protected final Communication io() {
    return session().getIO();
  }

  protected final Session session() {
    return Session.get();
  }

  protected void loop() {
    Session session = session();
    Communication io = io();
    setCompleter();
    while (session.isRunning()) {
      Result result;
      try {
        result = handleRequest(io.getRequest(prompt));
      } catch (Exception e) {
        result = new RequestExceptionFlag(e, ErrorCode.INVALID_INPUT);
      }
      switch (result) {
        case Flag flag:
          handleFlag(flag);
          break;
        case Response response:
          try {
            handleResponse(response);
          } catch (Exception e) {
            handleFlag(new CommandExceptionFlag(e, ErrorCode.INTERNAL_ERROR));
          }
          break;
      }
    }
  }

  protected abstract void stop();

  protected void handleFlag(Flag flag) {
    capabilityMap.entrySet().stream()
        .filter(e -> e.getKey().isAssignableFrom(flag.getClass()))
        .map(Map.Entry::getValue)
        .findFirst()
        .orElse(defaultFlagHandler)
        .handleFlag(flag);
  }

  protected void handleResponse(Response response) {
    io().printResponse(response);
  }

  public Layer getRootLayer() {
    return rootLayer;
  }

  public static class SelectAppType {
    public BifroestCliApp.NameSetter cli() {
      return new BifroestCliApp.TerminalRunnerBuilder();
    }

    public BifroestSSHApp.NameSetter ssh() {
      return new BifroestSSHApp.SshAppBuilder();
    }
  }
}
