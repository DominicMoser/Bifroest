package com.dmoser.codyssey.bifroest.runners;

import static com.dmoser.codyssey.bifroest.session.Session.get;

import com.dmoser.codyssey.bifroest.commands.*;
import com.dmoser.codyssey.bifroest.flags.*;
import com.dmoser.codyssey.bifroest.flags.handlers.*;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Response;
import com.dmoser.codyssey.bifroest.io.Result;
import com.dmoser.codyssey.bifroest.layers.NewLayer;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class AbstractRunner {

  protected NewLayer rootLayer;
  protected Map<Class<? extends Flag>, FlagHandler> flagHandlers = new HashMap<>();
  protected FlagHandler defaultFlagHandler = new UnhandledFlagHandler();
  protected Map<Pattern, Command> globalCommands = new HashMap<>();
  protected Prompt prompt;

  public AbstractRunner(NewLayer rootLayer, Prompt prompt) {
    this.rootLayer = rootLayer;
    this.prompt = prompt;
  }

  protected void loadFlagHandlers() {
    flagHandlers.put(NavigationFlag.class, new NavigationFlagHandler(this));
    flagHandlers.put(HelpFlag.class, new HelpFlagHandler());
    flagHandlers.put(ListCommandsFlag.class, new ListCommandsFlagHandler());
    flagHandlers.put(ExitFlag.class, new ExitFlagHandler());
    flagHandlers.put(ClearFlag.class, new ClearFlagHandler());
  }

  protected abstract void loadGlobalCommands();

  protected Result handleRequest(Request request) {

    // get active layer from root layer + origin
    List<String> currentPath = new ArrayList<>(get().getCurrentPath());
    NewLayer currentLayer = rootLayer;

    while (!currentPath.isEmpty() && currentLayer.hasLayer(currentPath.getFirst())) {
      currentLayer = currentLayer.getLayer(currentPath.getFirst());
      currentPath.removeFirst();
    }

    while (currentLayer.hasLayer(request.getCommand())) {
      currentLayer = currentLayer.getLayer(request.getCommand());
      if (!request.movePointer()) {
        List<String> destPath = new ArrayList<>(get().getCurrentPath());
        return Flags.navigationFlag(destPath, request.getCommand());
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

    return Flags.COMMAND_NOT_FOUND;
  }

  public void run() {
    init();
    loadFlagHandlers();
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
    while (session.isRunning()) {
      Result result;
      try {
        setCompleter();
        result = handleRequest(io.getRequest(prompt));
      } catch (Exception e) {
        result = Flags.requestExceptionFlag(e);
      }
      switch (result) {
        case Flag flag:
          handleFlag(flag);
          break;
        case Response response:
          try {
            handleResponse(response);
          } catch (Exception e) {
            handleFlag(Flags.commandExceptionFlag(e));
          }
          break;
      }
    }
  }

  protected abstract void stop();

  protected void handleFlag(Flag flag) {
    flagHandlers.getOrDefault(flag.getClass(), defaultFlagHandler).handleFlag(flag);
  }

  protected void handleResponse(Response response) {
    io().printResponse(response);
  }

  public NewLayer getRootLayer() {
    return rootLayer;
  }
}
