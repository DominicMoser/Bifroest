package com.dmoser.codyssey.bifroest.app;

import static com.dmoser.codyssey.bifroest.session.Session.get;

import com.dmoser.codyssey.bifroest.capabilities.*;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Response;
import com.dmoser.codyssey.bifroest.io.Result;
import com.dmoser.codyssey.bifroest.io.communications.RequestOrigin;
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

    Layer startLayer =
        request.pathOrigin() == RequestOrigin.ROOT ? rootLayer : resolveActiveLayer();
    List<String> path = request.path();
    int index = 0;
    Layer currentLayer = startLayer;

    while (index < path.size()) {
      if (!currentLayer.hasLayer(path.get(index))) {
        return new CommandNotFoundFlag(ErrorCode.COMMAND_NOT_FOUND, "path not found");
      }
      currentLayer = currentLayer.getLayer(path.get(index));
      if (!currentLayer.isAccessible(path.get(index))) {
        return new CommandNotFoundFlag(ErrorCode.COMMAND_NOT_FOUND, "path not accesible");
      }
      index++;
    }
    // the command is a layer and not a command. so we move there.
    if (currentLayer.hasLayer(request.command())) {
      Layer layer = currentLayer.getLayer(request.command());
      if (layer.isAccessible(request.command())) {
        List<String> navPath =
            switch (request.pathOrigin()) {
              case ROOT -> new ArrayList<>();
              default -> new ArrayList<>(Session.get().getCurrentPath());
            };
        navPath.addAll(request.path());
        navPath.add(request.command());
        return new NavigationFlag(navPath);
      }
    }

    if (currentLayer.hasCommand(request.command())) {
      Command command = currentLayer.getCommand(request.command());
      /*if (command.isAccessible(request.command())) {
        return new CommandNotFoundFlag(ErrorCode.COMMAND_NOT_FOUND, "");
      }*/
      return currentLayer.getCommand(request.command()).execute(request);
    }

    // Path not found. try global command, but only if path is empty
    if (request.path().isEmpty()) {
      for (Map.Entry<Pattern, Command> entry : globalCommands.entrySet()) {
        if (entry.getKey().matcher(request.command()).matches()) {
          return entry.getValue().execute(request);
        }
      }
    }

    return new CommandNotFoundFlag(ErrorCode.COMMAND_NOT_FOUND, request.command());
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
        Request request = io.getRequest(prompt);
        result = handleRequest(request);
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
