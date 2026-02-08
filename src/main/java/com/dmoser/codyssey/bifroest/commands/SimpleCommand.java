package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Response;
import com.dmoser.codyssey.bifroest.io.Result;
import java.util.List;

public interface SimpleCommand extends Command {
  default Result execute(Request request) {
    return (Response) () -> String.valueOf(simpleCommandExecute(request.getArguments()));
  }

  Object simpleCommandExecute(List<String> arguments);
}
