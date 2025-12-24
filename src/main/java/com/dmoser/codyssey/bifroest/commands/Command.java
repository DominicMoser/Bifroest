package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.enums.ExecutionSource;
import com.dmoser.codyssey.bifroest.layers.Layer;
import java.util.List;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;

public interface Command {

  String getNameRegex();

  ExecutionSource execute(LineReader lineReader, Layer parent, List<String> command);

  Completers.TreeCompleter.Node getCompleterNode();
}
