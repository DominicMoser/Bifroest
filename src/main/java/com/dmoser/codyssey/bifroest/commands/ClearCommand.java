package com.dmoser.codyssey.bifroest.commands;

import static org.jline.builtins.Completers.TreeCompleter.node;

import com.dmoser.codyssey.bifroest.enums.ExecutionSource;
import com.dmoser.codyssey.bifroest.layers.Layer;
import com.dmoser.codyssey.bifroest.session.Context;
import java.util.List;
import org.jline.builtins.Completers;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

public class ClearCommand implements Command {
  @Override
  public String getNameRegex() {
    return "(^clear$)|(^cl$)";
  }

  @Override
  public ExecutionSource execute(Layer parent, List<String> command) {
    Terminal terminal = Context.get().getTerminal();
    terminal.puts(InfoCmp.Capability.clear_screen);
    terminal.flush();
    return null;
  }

  @Override
  public Completers.TreeCompleter.Node getCompleterNode() {
    return node("clear", "cl");
  }
}
