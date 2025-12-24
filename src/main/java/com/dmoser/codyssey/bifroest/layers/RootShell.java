package com.dmoser.codyssey.bifroest.layers;

import com.dmoser.codyssey.bifroest.flags.ShellNavigationFlag;
import com.dmoser.codyssey.bifroest.flags.SystemExitFlag;
import java.util.ArrayList;
import java.util.List;

public class RootShell extends Layer {

  public RootShell() {
    super("rootShell");
  }

  @Override
  public String getLocation() {
    return "";
  }

  public void start() {

    List<String> path = new ArrayList<>(List.of(this.layerName));
    while (true) {
      try {
        super.execute(null, path);
        path = new ArrayList<>(List.of(this.layerName));
      } catch (SystemExitFlag exit) {
        // Exiting shell
        break;
      } catch (ShellNavigationFlag nav) {

        // Target could be / or /snp
        List<String> target = new ArrayList<>(nav.getTarget());
        if (checkForPath(target)) {
          target.addFirst(this.layerName);
          path = target;
          continue;
        }
        List<String> origin = new ArrayList<>(nav.getOrigin());
        origin.addFirst(this.layerName);
        path = origin;
      }
    }
  }
}
