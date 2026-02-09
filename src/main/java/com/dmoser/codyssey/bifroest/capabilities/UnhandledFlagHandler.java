package com.dmoser.codyssey.bifroest.capabilities;

import com.dmoser.codyssey.bifroest.io.Error;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.io.errors.ErrorCode;
import com.dmoser.codyssey.bifroest.session.Session;

public class UnhandledFlagHandler implements Capability {

  @Override
  public void handleFlag(Flag flag) {
    Session.io()
        .printError(
            new Error(
                ErrorCode.UNHANDLED_FLAG,
                "(" + flag.getClass().getSimpleName() + ") Flag unhandled"));
  }
}
