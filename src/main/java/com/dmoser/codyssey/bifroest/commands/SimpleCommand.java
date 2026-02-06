package com.dmoser.codyssey.bifroest.commands;

import java.util.List;

public interface SimpleCommand {
  Object execute(List<String> params);
}
