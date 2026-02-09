package com.dmoser.codyssey.bifroest.testUtils;

import com.dmoser.codyssey.bifroest.structure.Command;
import com.dmoser.codyssey.bifroest.structure.ComplexCommand;
import com.dmoser.codyssey.bifroest.structure.Layer;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class TestLayer implements Layer {

  private Map<String, Layer> layers;
  private Map<String, Command> commands;

  public TestLayer(Map<String, Layer> layers, Map<String, Command> commands) {
    this.layers = layers;
    this.commands = commands;
  }

  @Override
  public boolean hasLayer(String nameRegex) {
    return layers.containsKey(nameRegex);
  }

  @Override
  public Layer getLayer(String nameRegex) {
    return layers.get(nameRegex);
  }

  @Override
  public boolean hasCommand(String nameRegex) {
    return commands.containsKey(nameRegex);
  }

  @Override
  public Command getCommand(String nameRegex) {
    return commands.get(nameRegex);
  }

  @Override
  public Set<String> getCommandNames() {
    return commands.keySet();
  }

  @Override
  public Set<String> getLayerNames() {
    return layers.keySet();
  }

  @Override
  public void addCommand(ComplexCommand complexCommand) {}

  @Override
  public void addCommand(String name, Command command) {}

  @Override
  public void addCommand(String name, Pattern commandPattern, Command command) {}

  @Override
  public void addLayer(String name, Layer newLayer) {}

  @Override
  public void addLayer(String name, Pattern layerPattern, Layer newLayer) {}
}
