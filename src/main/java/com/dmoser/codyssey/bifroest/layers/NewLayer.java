package com.dmoser.codyssey.bifroest.layers;

import com.dmoser.codyssey.bifroest.commands.Command;
import com.dmoser.codyssey.bifroest.commands.ComplexCommand;
import java.util.Set;
import java.util.regex.Pattern;

public interface NewLayer {

  boolean hasLayer(String nameRegex);

  NewLayer getLayer(String nameRegex);

  boolean hasCommand(String nameRegex);

  Command getCommand(String nameRegex);

  Set<String> getCommandNames();

  Set<String> getLayerNames();

  void addCommand(ComplexCommand complexCommand);

  void addCommand(String name, Command command);

  void addCommand(String name, Pattern commandPattern, Command command);

  void addLayer(String name, NewLayer newLayer);

  void addLayer(String name, Pattern layerPattern, NewLayer newLayer);
}
