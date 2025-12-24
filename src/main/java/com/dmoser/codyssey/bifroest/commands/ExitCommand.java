package com.dmoser.codyssey.bifroest.commands;


import com.dmoser.codyssey.bifroest.enums.ExecutionSource;
import com.dmoser.codyssey.bifroest.flags.Flags;
import com.dmoser.codyssey.bifroest.layers.Layer;
import java.util.List;

/**
 * Command used to exit the current session.
 *
 * <p>When executed, this command throws a {@link com.dmoser.codyssey.bifroest.flags.SystemExitFlag}
 * (via {@link Flags#SYSTEM_EXIT_FLAG}) to signal that the entire CLI session should be terminated.
 */
public class ExitCommand implements Command {

  @Override
  public ExecutionSource execute(Layer parent, List<String> command) {
    throw Flags.SYSTEM_EXIT_FLAG;
  }

  @Override
  public String getName() {
    return "exit";
  }
}
