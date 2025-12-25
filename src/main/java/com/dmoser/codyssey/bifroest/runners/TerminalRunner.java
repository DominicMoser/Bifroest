package com.dmoser.codyssey.bifroest.runners;

import com.dmoser.codyssey.bifroest.banner.Banner;
import com.dmoser.codyssey.bifroest.banner.SimpleContextNameBanner;
import com.dmoser.codyssey.bifroest.layers.RootShell;
import java.io.IOException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class TerminalRunner extends AbstractRunner {

  RootShell rootShell;
  Banner banner;
  String name;

  private TerminalRunner(String name, RootShell rootShell, Banner banner) {
    this.banner = banner;
    this.name = name;
    this.rootShell = rootShell;
  }

  public static NameSetter builder() {
    return new TerminalRunnerBuilder();
  }

  @Override
  protected RootShell getRootShell() {
    return this.rootShell;
  }

  @Override
  protected LineReader getLineReader() {
    try {
      Terminal terminal = TerminalBuilder.builder().build();
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
      return reader;

    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override
  protected Banner getBanner() {
    return this.banner;
  }

  @Override
  protected String getName() {
    return name;
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
