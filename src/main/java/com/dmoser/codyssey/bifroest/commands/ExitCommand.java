package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.flags.Flags;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;

/**
 * Command used to exit the current session.
 *
 * <p>When executed, this command throws a {@link com.dmoser.codyssey.bifroest.flags.SystemExitFlag}
 * (via {@link Flags#SYSTEM_EXIT_FLAG}) to signal that the entire CLI session should be terminated.
 */
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
