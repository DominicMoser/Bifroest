package com.dmoser.codyssey.bifroest.flags.handlers;

import com.dmoser.codyssey.bifroest.flags.FlagHandler;
import com.dmoser.codyssey.bifroest.io.Error;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.session.Session;

public class UnhandledFlagHandler implements FlagHandler {

  @Override
  public void handleFlag(Flag flag) {
    Session.io()
        .printError(
            new Error(
                Error.ERROR_UNHANDLED, "(" + flag.getClass().getSimpleName() + ") Flag unhandled"));
  }
}
