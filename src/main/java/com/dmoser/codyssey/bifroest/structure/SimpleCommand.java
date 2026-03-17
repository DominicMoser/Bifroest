package com.dmoser.codyssey.bifroest.structure;

import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Response;
import com.dmoser.codyssey.bifroest.io.Result;

public interface SimpleCommand extends Command {
  default Result execute(Request request) {
    return (Response) () -> String.valueOf(simpleCommandExecute(request));
  }

  Object simpleCommandExecute(Request request);
}
