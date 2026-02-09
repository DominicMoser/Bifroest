package com.dmoser.codyssey.bifroest.structure;

public interface ComplexCommand extends Command {
  String getRegex();

  String getName();
}
