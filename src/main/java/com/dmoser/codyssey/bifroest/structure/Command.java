package com.dmoser.codyssey.bifroest.structure;

import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;

public interface Command {
  Result execute(Request request);
}
