package com.dmoser.codyssey.bifroest.structure;

import com.dmoser.codyssey.bifroest.io.flags.ListCommandsFlag;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;

public class LsCommand implements ComplexCommand {

  private final Layer newLayer;

  public LsCommand(Layer newLayer) {
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
