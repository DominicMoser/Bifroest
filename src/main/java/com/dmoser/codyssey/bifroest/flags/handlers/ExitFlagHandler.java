package com.dmoser.codyssey.bifroest.flags.handlers;

import com.dmoser.codyssey.bifroest.flags.FlagHandler;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.session.Session;

public class ExitFlagHandler implements FlagHandler {
  @Override
  public void handleFlag(Flag flag) {
    Session.get().stop();
  }
}
