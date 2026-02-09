package com.dmoser.codyssey.bifroest.capabilities;

import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.session.Session;

public class ExitFlagHandler implements Capability {
  @Override
  public void handleFlag(Flag flag) {
    Session.get().stop();
  }
}
