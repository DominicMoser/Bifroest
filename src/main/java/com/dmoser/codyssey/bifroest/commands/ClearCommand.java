package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.flags.Flags;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;

public class ClearCommand implements ComplexCommand {

  @Override
  public Result execute(Request request) {
    return Flags.CLEAR_FLAG;
  }

  @Override
  public String getRegex() {
    return "^" + getName() + "$";
  }

  @Override
  public String getName() {
    return "clear";
  }
}
