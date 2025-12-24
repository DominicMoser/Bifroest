package com.dmoser.codyssey.bifroest.commands;

import static org.jline.builtins.Completers.TreeCompleter.node;

import com.dmoser.codyssey.bifroest.enums.ExecutionSource;
import com.dmoser.codyssey.bifroest.flags.Flags;
import com.dmoser.codyssey.bifroest.layers.Layer;
import java.util.List;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;

/**
 * A default command that exists in every layer, used to navigate up one shell layer.
 *
 * <p>This command can be triggered by either "up" or ".." and works by throwing a {@link
 * com.dmoser.codyssey.bifroest.flags.ShellExitFlag}.
 */
public class UpCommand implements Command {

  @Override
  public String getNameRegex() {
    return "(^\\.\\.$)|(^up$)";
  }

  @Override
  public ExecutionSource execute(LineReader lineReader, Layer parent, List<String> command) {
    throw Flags.SHELL_EXIT_FLAG;
  }

  @Override
  public Completers.TreeCompleter.Node getCompleterNode() {
    return node("..", "up");
  }
}
