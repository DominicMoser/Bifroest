package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;

public interface Command {
  Result execute(Request request);
}
