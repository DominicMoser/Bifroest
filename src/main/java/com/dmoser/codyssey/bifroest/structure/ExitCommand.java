package com.dmoser.codyssey.bifroest.structure;

import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;
import com.dmoser.codyssey.bifroest.io.flags.Flags;

public class ExitCommand implements ComplexCommand {

  @Override
  public Result execute(Request request) {
    return Flags.EXIT_FLAG;
  }

  @Override
  public String getRegex() {
    return "^" + getName() + "$";
  }

  @Override
  public String getName() {
    return "exit";
  }
}
