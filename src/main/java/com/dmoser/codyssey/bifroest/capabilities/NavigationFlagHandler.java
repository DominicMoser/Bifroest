package com.dmoser.codyssey.bifroest.capabilities;

import com.dmoser.codyssey.bifroest.app.BifroestApp;
import com.dmoser.codyssey.bifroest.io.flags.NavigationFlag;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.session.Session;
import com.dmoser.codyssey.bifroest.structure.Layer;

import java.util.ArrayList;
import java.util.List;

public class NavigationFlagHandler implements Capability {
  protected final BifroestApp bifroestApp;

  public NavigationFlagHandler(BifroestApp bifroestApp) {
    this.bifroestApp = bifroestApp;
  }

  @Override
  public void handleFlag(Flag flag) {
    NavigationFlag navigationFlag = (NavigationFlag) flag;
    List<String> currentPath = new ArrayList<>(navigationFlag.path());
    Layer currentLayer = bifroestApp.getRootLayer();
    while (!currentPath.isEmpty() && currentLayer.hasLayer(currentPath.getFirst())) {
      currentLayer = currentLayer.getLayer(currentPath.getFirst());
      currentPath.removeFirst();
    }
    if (currentPath.isEmpty()) {
      Session.get().setCurrentPath(((NavigationFlag) flag).path());
    }
  }
}
