package com.dmoser.codyssey.bifroest.commands;

import static org.jline.builtins.Completers.TreeCompleter.node;

import com.dmoser.codyssey.bifroest.layers.Layer;
import com.dmoser.codyssey.bifroest.returns.CommandReturn;
import java.util.List;
import org.jline.builtins.Completers;

public interface Command {

  default String getNameRegex() {
    return "^" + getName() + "$";
  }

  CommandReturn execute(Layer parent, List<String> command);

  default Completers.TreeCompleter.Node getCompleterNode() {
    return node(getName());
  }

  String getName();
}
