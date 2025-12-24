package com.dmoser.codyssey.bifroest.runners;

import com.dmoser.codyssey.bifroest.banner.Banner;
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

  public TerminalRunner(String name) {
    Context.get().setName(name);
  }

  public TerminalRunner(String name, Banner banner) {
    Context.get().setName(name);
    Context.get().setBanner(banner);
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
      RootShell rootShell = new RootShell(reader);
      rootShell.start();
      terminal.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
