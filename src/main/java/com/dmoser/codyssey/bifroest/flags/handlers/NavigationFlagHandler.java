package com.dmoser.codyssey.bifroest.flags.handlers;

import com.dmoser.codyssey.bifroest.flags.FlagHandler;
import com.dmoser.codyssey.bifroest.flags.NavigationFlag;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.layers.NewLayer;
import com.dmoser.codyssey.bifroest.runners.AbstractRunner;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.ArrayList;
import java.util.List;

public class NavigationFlagHandler implements FlagHandler {
  protected final AbstractRunner abstractRunner;

  public NavigationFlagHandler(AbstractRunner abstractRunner) {
    this.abstractRunner = abstractRunner;
  }

  @Override
  public void handleFlag(Flag flag) {
    NavigationFlag navigationFlag = (NavigationFlag) flag;
    List<String> currentPath = new ArrayList<>(navigationFlag.path());
    NewLayer currentLayer = abstractRunner.getRootLayer();
    while (!currentPath.isEmpty() && currentLayer.hasLayer(currentPath.getFirst())) {
      currentLayer = currentLayer.getLayer(currentPath.getFirst());
      currentPath.removeFirst();
    }
    if (currentPath.isEmpty()) {
      Session.get().setCurrentPath(((NavigationFlag) flag).path());
    }
  }
}
