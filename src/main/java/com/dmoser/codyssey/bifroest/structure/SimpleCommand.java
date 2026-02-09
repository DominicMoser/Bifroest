package com.dmoser.codyssey.bifroest.structure;

import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Response;
import com.dmoser.codyssey.bifroest.io.Result;
import java.util.List;

public interface SimpleCommand extends Command {
  default Result execute(Request request) {
    var path = request.getPath();
    var origin = request.getOrigin();
    var args = request.getArguments();
    return (Response)
        () -> String.valueOf(simpleCommandExecute(request.getPath(), request.getArguments()));
  }

  Object simpleCommandExecute(List<String> path, List<String> arguments);
}
