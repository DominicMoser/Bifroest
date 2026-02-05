package com.dmoser.codyssey.bifroest.commands;

import static org.jline.builtins.Completers.TreeCompleter.node;

import com.dmoser.codyssey.bifroest.layers.Layer;
import com.dmoser.codyssey.bifroest.returns.CommandReturn;
import com.dmoser.codyssey.bifroest.session.Session;
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
  public CommandReturn execute(Layer parent, List<String> command) {
    Terminal terminal = Session.get().getTerminal();
    terminal.puts(InfoCmp.Capability.clear_screen);
    terminal.flush();
    return null;
  }

  @Override
  public Completers.TreeCompleter.Node getCompleterNode() {
    return node("clear", "cl");
  }

  @Override
  public String getName() {
    return "clear";
  }
}
