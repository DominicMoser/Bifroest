package com.dmoser.codyssey.bifroest.capabilities;

import com.dmoser.codyssey.bifroest.io.Error;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.io.errors.ErrorCode;
import com.dmoser.codyssey.bifroest.io.flags.ErrorFlag;
import com.dmoser.codyssey.bifroest.io.flags.ExceptionFlag;
import com.dmoser.codyssey.bifroest.session.Session;

public class ErrorFlagHandler implements Capability {
  @Override
  public void handleFlag(Flag flag) {
    if (flag instanceof ExceptionFlag exceptionFlag) {
      Session.get().getIO().printError(new Error(exceptionFlag.errorCode()));
      return;
    }
    if (flag instanceof ErrorFlag errorFlag) {
      Session.get().getIO().printError(new Error(errorFlag.errorCode()));
      return;
    }
    Session.get().getIO().printError(new Error(ErrorCode.UNKNOWN));
  }
}
