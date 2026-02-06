package com.dmoser.codyssey.bifroest.commands;

import static org.jline.builtins.Completers.TreeCompleter.node;

import com.dmoser.codyssey.bifroest.layers.Layer;
import com.dmoser.codyssey.bifroest.returns.CommandReturn;
import com.dmoser.codyssey.bifroest.returns.ReturnStatus;
import com.dmoser.codyssey.bifroest.returns.RoutingFlag;
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
  public CommandReturn execute(Layer parent, List<String> command) {
    List<String> params = new ArrayList<>(command);
    params.removeFirst();
    Object returnValue = simpleCommand.execute(params);
    if (returnValue instanceof ReturnStatus.Failure failure) {
      return new CommandReturn(RoutingFlag.RETURN, failure, null);
    }
    return new CommandReturn(RoutingFlag.RETURN, ReturnStatus.SUCCESS, returnValue);
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
