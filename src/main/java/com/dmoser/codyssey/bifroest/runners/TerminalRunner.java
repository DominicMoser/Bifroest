package com.dmoser.codyssey.bifroest.runners;

import com.dmoser.codyssey.bifroest.banner.Banner;
import com.dmoser.codyssey.bifroest.banner.SimpleContextNameBanner;
import com.dmoser.codyssey.bifroest.commands.ClearCommand;
import com.dmoser.codyssey.bifroest.commands.ExitCommand;
import com.dmoser.codyssey.bifroest.commands.NavigationCommand;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.communications.TerminalCommunication;
import com.dmoser.codyssey.bifroest.io.completer.Root;
import com.dmoser.codyssey.bifroest.layers.NewLayer;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.regex.Pattern;

public class TerminalRunner extends AbstractRunner {

  private String name;
  private Banner banner;

  private TerminalRunner(String name, NewLayer rootLayer, Banner banner, Prompt prompt) {
    super(rootLayer, prompt);
    this.banner = banner;
    this.name = name;
  }

  public static TerminalRunner.NameSetter builder() {
    return new TerminalRunner.TerminalRunnerBuilder();
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
  public void setCompleter() {
    Root root = new Root(this.rootLayer.getCommandNames());
    io().setCompleter(root);
  }

  @Override
  protected void initSession() {

    Communication communication = new TerminalCommunication();

    Session.create()
        .withName(this.name)
        .andCommunication(communication)
        .andBanner(this.banner)
        .open();
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
    TerminalRunner.RootLayerSetter withName(String name);
  }

  public interface RootLayerSetter {
    TerminalRunner.OptionalFieldsSetter andRootShell(NewLayer rootLayer);
  }

  public interface OptionalFieldsSetter {
    TerminalRunner.OptionalFieldsSetter andBanner(Banner banner);

    TerminalRunner.OptionalFieldsSetter andPrompt(Prompt prompt);

    TerminalRunner build();
  }

  private static class TerminalRunnerBuilder
      implements TerminalRunner.RootLayerSetter,
          TerminalRunner.NameSetter,
          TerminalRunner.OptionalFieldsSetter {

    NewLayer rootLayer = null;
    String name = null;
    Banner banner = new SimpleContextNameBanner();
    Prompt prompt = Prompt.DEFAULT;

    @Override
    public TerminalRunner.OptionalFieldsSetter andBanner(Banner banner) {
      this.banner = banner;
      return this;
    }

    @Override
    public OptionalFieldsSetter andPrompt(Prompt prompt) {
      this.prompt = prompt;
      return this;
    }

    @Override
    public TerminalRunner build() {
      return new TerminalRunner(name, rootLayer, banner, prompt);
    }

    @Override
    public TerminalRunner.OptionalFieldsSetter andRootShell(NewLayer rootLayer) {
      this.rootLayer = rootLayer;
      return this;
    }

    @Override
    public TerminalRunner.RootLayerSetter withName(String name) {
      this.name = name;
      return this;
    }
  }
}
