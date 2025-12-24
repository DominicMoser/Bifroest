package com.dmoser.codyssey.bifroest.commands;

import static org.jline.builtins.Completers.TreeCompleter.node;

import com.dmoser.codyssey.bifroest.enums.ExecutionSource;
import com.dmoser.codyssey.bifroest.layers.Layer;
import com.dmoser.codyssey.bifroest.session.Context;
import java.util.ArrayList;
import java.util.List;
import org.jline.builtins.Completers;

public class SimpleCommandContainer implements Command {

  private final String name;
  private final SimpleCommand simpleCommand;

  public SimpleCommandContainer(String name, SimpleCommand simpleCommand) {
    this.name = name;
    this.simpleCommand = simpleCommand;
  }

  @Override
  public String getNameRegex() {
    return "^" + name + "$";
  }

  @Override
  public ExecutionSource execute(Layer parent, List<String> command) {
    List<String> params = new ArrayList<>(command);
    params.removeFirst();
    simpleCommand.execute(
        params, Context.get().getLineReader(), Context.get().getTerminal().writer());
    return ExecutionSource.COMMAND;
  }

  @Override
  public Completers.TreeCompleter.Node getCompleterNode() {
    return node(this.name);
  }

  @Override
  public String getName() {
    return name;
  }
}
