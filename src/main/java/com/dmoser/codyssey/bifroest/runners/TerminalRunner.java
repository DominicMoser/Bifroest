package com.dmoser.codyssey.bifroest.runners;

import com.dmoser.codyssey.bifroest.banner.Banner;
import com.dmoser.codyssey.bifroest.banner.SimpleContextNameBanner;
import com.dmoser.codyssey.bifroest.layers.RootShell;
import com.dmoser.codyssey.bifroest.session.Context;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

public class TerminalRunner {

  RootShell rootShell;

  private TerminalRunner(String name, RootShell rootShell, Banner banner) {
    Context.get().setName(name);
    Context.get().setBanner(banner);
    this.rootShell = rootShell;
  }

  public static NameSetter builder() {
    return new TerminalRunnerBuilder();
  }

  /**
   * Starts the terminal runner.
   *
   * <p>Note: Native Jansi support is disabled to avoid restricted method warnings in newer JDKs.
   * ANSI support is provided by JLine's built-in providers.
   */
  public void start() {
    try {
      System.setProperty("org.jline.terminal.jansi", "false");
      Terminal terminal = TerminalBuilder.builder().build();

      // Clear screen
      terminal.puts(InfoCmp.Capability.clear_screen);
      terminal.flush();

      terminal.writer().println(Context.get().getBanner().getString());
      terminal.writer().flush();

      // Create a line reader
      LineReader reader =
          LineReaderBuilder.builder()
              .terminal(terminal)
              .variable(LineReader.LIST_MAX, 50) // max tab completion candidates
              .completer(
                  new ArgumentCompleter(
                      new StringsCompleter("exit", "help"), NullCompleter.INSTANCE))
              .option(LineReader.Option.AUTO_LIST, true) // Automatically list options
              .option(LineReader.Option.LIST_PACKED, true) // Display completions in a compact form
              .option(LineReader.Option.AUTO_MENU, true) // Show menu automatically
              .option(LineReader.Option.MENU_COMPLETE, true) // Cycle through completions
              .build();

      Context.get().setLineReader(reader);
      Context.get().setTerminal(terminal);

      rootShell.start();
      terminal.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public interface NameSetter {
    RootShellSetter withName(String name);
  }

  public interface RootShellSetter {
    OptionalFieldsSetter andRootShell(RootShell rootShell);
  }

  public interface OptionalFieldsSetter {
    OptionalFieldsSetter andBanner(Banner banner);

    TerminalRunner build();
  }

  private static class TerminalRunnerBuilder
      implements RootShellSetter, NameSetter, OptionalFieldsSetter {

    RootShell rootShell = null;
    String name = null;
    Banner banner = new SimpleContextNameBanner();

    @Override
    public OptionalFieldsSetter andBanner(Banner banner) {
      this.banner = banner;
      return this;
    }

    @Override
    public TerminalRunner build() {
      return new TerminalRunner(name, rootShell, banner);
    }

    @Override
    public OptionalFieldsSetter andRootShell(RootShell rootShell) {
      this.rootShell = rootShell;
      return this;
    }

    @Override
    public RootShellSetter withName(String name) {
      this.name = name;
      return this;
    }
  }
}
