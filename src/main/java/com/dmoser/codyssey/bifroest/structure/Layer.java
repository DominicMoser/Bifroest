package com.dmoser.codyssey.bifroest.structure;

import com.dmoser.codyssey.bifroest.structure.Command;
import com.dmoser.codyssey.bifroest.structure.ComplexCommand;
import java.util.Set;
import java.util.regex.Pattern;

public interface Layer {

  boolean hasLayer(String nameRegex);

  Layer getLayer(String nameRegex);

  boolean hasCommand(String nameRegex);

  Command getCommand(String nameRegex);

  Set<String> getCommandNames();

  Set<String> getLayerNames();

  void addCommand(ComplexCommand complexCommand);

  void addCommand(String name, Command command);

  void addCommand(String name, Pattern commandPattern, Command command);

  void addLayer(String name, Layer newLayer);

  void addLayer(String name, Pattern layerPattern, Layer newLayer);

  default boolean isAccessible(String command) {
    return true;
  }
}
