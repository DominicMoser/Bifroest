package com.dmoser.codyssey.bifroest.flags.handlers;

import com.dmoser.codyssey.bifroest.flags.FlagHandler;
import com.dmoser.codyssey.bifroest.flags.ListCommandsFlag;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.layers.NewLayer;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListCommandsFlagHandler implements FlagHandler {
  @Override
  public void handleFlag(Flag flag) {
    ListCommandsFlag lsFlag = (ListCommandsFlag) flag;
    NewLayer layer = lsFlag.layer();
    Communication io = Session.get().getIO();

    // Build Response
    String response =
        Stream.concat(layer.getCommandNames().stream(), layer.getLayerNames().stream())
            .collect(Collectors.joining(",", "[", "]"));
    //

    io.printResponse(() -> response);
  }
}
