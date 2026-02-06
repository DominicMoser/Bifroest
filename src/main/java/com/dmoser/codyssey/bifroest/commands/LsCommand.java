package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.layers.Layer;
import com.dmoser.codyssey.bifroest.returns.CommandReturn;
import com.dmoser.codyssey.bifroest.returns.ReturnStatus;
import com.dmoser.codyssey.bifroest.returns.RoutingFlag;
import java.util.List;
import java.util.stream.Collectors;

public class LsCommand implements Command {

  @Override
  public CommandReturn execute(Layer parent, List<String> command) {
    String lsOutput = parent.getCommandNames().stream().collect(Collectors.joining(",", "[", "]"));
    return new CommandReturn(RoutingFlag.RETURN, ReturnStatus.SUCCESS, lsOutput);
  }

  @Override
  public String getName() {
    return "ls";
  }
}
