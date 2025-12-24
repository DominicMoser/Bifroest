package com.dmoser.codyssey.bifroest.commands;

import static org.jline.builtins.Completers.TreeCompleter.node;

import com.dmoser.codyssey.bifroest.enums.ExecutionSource;
import com.dmoser.codyssey.bifroest.layers.Layer;
import java.util.List;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

public class ClearCommand implements Command {
  @Override
  public String getNameRegex() {
    return "(^clear$)|(^cl$)";
  }

  @Override
  public ExecutionSource execute(LineReader lineReader, Layer parent, List<String> command) {
    Terminal terminal = lineReader.getTerminal();
    terminal.puts(InfoCmp.Capability.clear_screen);
    terminal.flush();
    return null;
  }

  @Override
  public Completers.TreeCompleter.Node getCompleterNode() {
    return node("clear", "cl");
  }
}
