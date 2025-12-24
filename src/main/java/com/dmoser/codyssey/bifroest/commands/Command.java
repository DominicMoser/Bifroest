package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.enums.ExecutionSource;
import com.dmoser.codyssey.bifroest.layers.Layer;
import java.util.List;
import org.jline.builtins.Completers;

public interface Command {

  String getNameRegex();

  ExecutionSource execute(Layer parent, List<String> command);

  Completers.TreeCompleter.Node getCompleterNode();
}
