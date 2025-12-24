package com.dmoser.codyssey.bifroest.commands;

import java.io.PrintWriter;
import java.util.List;
import org.jline.reader.LineReader;

public interface SimpleCommand {
  void execute(List<String> params, LineReader in, PrintWriter out);
}
