package com.dmoser.codyssey.bifroest.app;

import com.dmoser.codyssey.bifroest.capabilities.*;
import com.dmoser.codyssey.bifroest.forms.flags.FormFlag;
import com.dmoser.codyssey.bifroest.forms.flags.FormFlagHandler;
import com.dmoser.codyssey.bifroest.io.Banner;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.banners.SimpleContextNameBanner;
import com.dmoser.codyssey.bifroest.io.completer.CompleterProviderImplementation;
import com.dmoser.codyssey.bifroest.io.flags.*;
import com.dmoser.codyssey.bifroest.session.Session;
import com.dmoser.codyssey.bifroest.structure.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BifroestCliApp extends BifroestApp {

  private final String name;
  private final Banner banner;
  private final CompleterProviderImplementation completerProvider;
  private final Communication communication;

  protected BifroestCliApp(
      String name, Layer rootLayer, Communication communication, Banner banner, Prompt prompt) {
    super(rootLayer, prompt);
    this.banner = banner;
    this.name = name;
    this.completerProvider = new CompleterProviderImplementation();
    this.communication = communication;
  }

  @Override
  protected void loadCapabilities() {
    capabilityMap.put(NavigationFlag.class, new NavigationFlagHandler(this));
    capabilityMap.put(HelpFlag.class, new HelpFlagHandler());
    capabilityMap.put(ListCommandsFlag.class, new ListCommandsFlagHandler());
    capabilityMap.put(ExitFlag.class, new ExitFlagHandler());
    capabilityMap.put(ClearFlag.class, new ClearFlagHandler());
    capabilityMap.put(FormFlag.class, new FormFlagHandler());
    capabilityMap.put(ErrorFlag.class, new ErrorFlagHandler());
  }

  @Override
  protected void loadGlobalCommands() {
    NavigationCommand navigationCommand = new NavigationCommand();
    ClearCommand clearCommand = new ClearCommand();
    ExitCommand exitCommand = new ExitCommand();
    globalCommands.put(Pattern.compile(exitCommand.getRegex()), exitCommand);
    globalCommands.put(Pattern.compile(clearCommand.getRegex()), clearCommand);
    globalCommands.put(Pattern.compile(navigationCommand.getRegex()), navigationCommand);
  }

  @Override
  protected void init() {
    buildCompletionTree(rootLayer, List.of(), new java.util.HashSet<>());
  }

  private void buildCompletionTree(
      Layer layer, List<String> currentPath, java.util.Set<Layer> visited) {
    if (visited.contains(layer)) {
      return;
    }
    visited.add(layer);

    for (Command cmd : globalCommands.values()) {
      if (cmd instanceof ComplexCommand cc) {
        List<String> path = new ArrayList<>(currentPath);
        path.add(cc.getName());
        completerProvider.addPath(path);
      }
    }

    for (String commandName : layer.getCommandNames()) {
      List<String> path = new ArrayList<>(currentPath);
      path.add(commandName);
      completerProvider.addPath(path);
    }
    for (String layerName : layer.getLayerNames()) {
      Layer subLayer = layer.getLayer(layerName);
      if (subLayer != null) {
        List<String> path = new ArrayList<>(currentPath);
        path.add(layerName);
        completerProvider.addPath(path);
        buildCompletionTree(subLayer, path, visited);
      }
    }
    visited.remove(layer);
  }

  @Override
  public void setCompleter() {
    io().setCompleterProvider(completerProvider);
  }

  @Override
  protected void initSession() {
    Session.create().withName(this.name).andCommunication(communication).open();
  }

  @Override
  protected void start() {
    io().printBanner(this.banner);
  }

  @Override
  protected void stop() {
    Session.close();
  }

  public interface NameSetter {
    BifroestCliApp.CommunicationSetter withName(String name);
  }

  public interface CommunicationSetter {
    BifroestCliApp.RootLayerSetter andCommunication(Communication communication);
  }

  public interface RootLayerSetter {
    BifroestCliApp.OptionalFieldsSetter andEntryPoint(Layer rootLayer);
  }

  public interface OptionalFieldsSetter {
    BifroestCliApp.OptionalFieldsSetter andBanner(Banner banner);

    BifroestCliApp.OptionalFieldsSetter andPrompt(Prompt prompt);

    BifroestCliApp build();
  }

  static class TerminalRunnerBuilder
      implements BifroestCliApp.RootLayerSetter,
          BifroestCliApp.NameSetter,
          BifroestCliApp.CommunicationSetter,
          BifroestCliApp.OptionalFieldsSetter {

    Layer rootLayer = null;
    String name = null;
    Banner banner = new SimpleContextNameBanner();
    Prompt prompt = Prompt.DEFAULT;
    Communication communication;

    @Override
    public BifroestCliApp.OptionalFieldsSetter andBanner(Banner banner) {
      this.banner = banner;
      return this;
    }

    @Override
    public OptionalFieldsSetter andPrompt(Prompt prompt) {
      this.prompt = prompt;
      return this;
    }

    @Override
    public BifroestCliApp build() {
      return new BifroestCliApp(name, rootLayer, communication, banner, prompt);
    }

    @Override
    public BifroestCliApp.OptionalFieldsSetter andEntryPoint(Layer rootLayer) {
      this.rootLayer = rootLayer;
      return this;
    }

    @Override
    public BifroestCliApp.CommunicationSetter withName(String name) {
      this.name = name;
      return this;
    }

    @Override
    public RootLayerSetter andCommunication(Communication communication) {
      this.communication = communication;
      return this;
    }
  }
}
