package com.dmoser.codyssey.bifroest.testUtils;

import com.dmoser.codyssey.bifroest.commands.Command;
import com.dmoser.codyssey.bifroest.layers.NewLayer;
import java.util.Map;

public class TestLayer implements NewLayer {

  private Map<String, NewLayer> layers;
  private Map<String, Command> commands;

  public TestLayer(Map<String, NewLayer> layers, Map<String, Command> commands) {
    this.layers = layers;
    this.commands = commands;
  }

  @Override
  public boolean hasLayer(String nameRegex) {
    return layers.containsKey(nameRegex);
  }

  @Override
  public NewLayer getLayer(String nameRegex) {
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
}
