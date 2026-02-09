package com.dmoser.codyssey.bifroest.capabilities;

import com.dmoser.codyssey.bifroest.io.flags.ListCommandsFlag;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.structure.Layer;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListCommandsFlagHandler implements Capability {
  @Override
  public void handleFlag(Flag flag) {
    ListCommandsFlag lsFlag = (ListCommandsFlag) flag;
    Layer layer = lsFlag.layer();
    Communication io = Session.get().getIO();

    // Build Response
    String response =
        Stream.concat(layer.getCommandNames().stream(), layer.getLayerNames().stream())
            .collect(Collectors.joining(",", "[", "]"));
    //

    io.printResponse(() -> response);
  }
}
