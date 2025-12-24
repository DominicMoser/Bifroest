package com.dmoser.codyssey.bifroest.commands;

import static org.jline.builtins.Completers.TreeCompleter.node;

import com.dmoser.codyssey.bifroest.enums.ExecutionSource;
import com.dmoser.codyssey.bifroest.flags.ShellNavigationFlag;
import com.dmoser.codyssey.bifroest.layers.Layer;
import java.util.Arrays;
import java.util.List;
import org.jline.builtins.Completers;

/**
 * Command used to navigate between different shell layers.
 *
 * <p>This command matches path-like patterns and triggers navigation by throwing a {@link
 * ShellNavigationFlag} with the calculated target and origin paths.
 */
public class NavigationCommand implements Command {

  @Override
  public String getNameRegex() {
    return "^(/?\\w+)?((/\\w+)+)/?$|^(\\w*/)$";
  }

  @Override
  public ExecutionSource execute(Layer parent, List<String> command) {

    String path = command.getFirst();
    if (!path.startsWith("/")) {
      path = parent.getLocation() + "/" + path;
    }

    List<String> target = Arrays.stream(path.split("/")).filter(s -> !s.isEmpty()).toList();
    List<String> origin =
        Arrays.stream(parent.getLocation().split("/")).filter(s -> !s.isEmpty()).toList();
    throw new ShellNavigationFlag(target, origin);
  }

  @Override
  public Completers.TreeCompleter.Node getCompleterNode() {
    return node("/");
  }
}
