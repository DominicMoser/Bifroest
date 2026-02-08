package com.dmoser.codyssey.bifroest.commands;

public interface ComplexCommand extends Command {
  String getRegex();

  String getName();
}
