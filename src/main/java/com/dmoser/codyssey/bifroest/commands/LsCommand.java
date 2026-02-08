package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.flags.ListCommandsFlag;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;
import com.dmoser.codyssey.bifroest.layers.NewLayer;

public class LsCommand implements ComplexCommand {

  private final NewLayer newLayer;

  public LsCommand(NewLayer newLayer) {
    this.newLayer = newLayer;
  }

  @Override
  public Result execute(Request request) {
    return new ListCommandsFlag(newLayer);
  }

  @Override
  public String getRegex() {
    return "^" + getName() + "$";
  }

  @Override
  public String getName() {
    return "ls";
  }
}
