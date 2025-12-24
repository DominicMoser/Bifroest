package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.enums.ExecutionSource;
import com.dmoser.codyssey.bifroest.layers.Layer;
import com.dmoser.codyssey.bifroest.session.Context;
import java.util.List;
import java.util.stream.Collectors;

public class LsCommand implements Command {

  @Override
  public ExecutionSource execute(Layer parent, List<String> command) {
    String lsOutput = parent.getCommandNames().stream().collect(Collectors.joining(",", "[", "]"));
    Context.get().getTerminal().writer().println(lsOutput);
    return ExecutionSource.COMMAND;
  }

  @Override
  public String getName() {
    return "ls";
  }
}
