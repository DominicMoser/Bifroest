package com.dmoser.codyssey.bifroest.structure;

import com.dmoser.codyssey.bifroest.structure.*;
import java.util.*;
import java.util.regex.Pattern;

public abstract class AbstractLayer implements Layer {

  private Map<Pattern, Layer> layers = new HashMap<>();
  private Map<String, Layer> layerInfoMap = new HashMap<>();

  private Map<Pattern, Command> commands = new HashMap<>();
  private Map<String, Command> commandInfoMap = new HashMap<>();

  protected AbstractLayer() {
    addCommand(new LsCommand(this));
  }

  public Set<String> getCommandNames() {
    return commandInfoMap.keySet();
  }

  @Override
  public Set<String> getLayerNames() {
    return layerInfoMap.keySet();
  }

  public void addLayer(String name, Layer newLayer) {
    addLayer(name, Pattern.compile(name), newLayer);
  }

  public void addLayer(String name, Pattern layerPattern, Layer newLayer) {
    if (this.layerInfoMap.containsKey(name)) {
      // cant add another command with the same name.
      return;
    }
    this.layerInfoMap.put(name, newLayer);
    this.layers.put(layerPattern, newLayer);
  }

  public void addCommand(ComplexCommand command) {
    addCommand(command.getName(), Pattern.compile(command.getRegex()), command);
  }

  public void addCommand(String name, Command command) {
    addCommand(name, Pattern.compile("^" + name + "$"), command);
  }

  public void addCommand(String name, SimpleCommand command) {
    addCommand(name, Pattern.compile("^" + name + "$"), command);
  }

  public void addCommand(String name, Pattern pattern, Command command) {
    if (this.commandInfoMap.containsKey(name)) {
      // cant add another command with the same name.
      return;
    }
    this.commandInfoMap.put(name, command);
    this.commands.put(pattern, command);
  }

  @Override
  public boolean hasLayer(String nameRegex) {
    if (layerInfoMap.containsKey(nameRegex)) {
      return true;
    }
    for (Map.Entry<Pattern, Layer> entry : layers.entrySet()) {
      if (entry.getKey().matcher(nameRegex).matches()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Layer getLayer(String nameRegex) {
    if (layerInfoMap.containsKey(nameRegex)) {
      return layerInfoMap.get(nameRegex);
    }
    for (Map.Entry<Pattern, Layer> entry : layers.entrySet()) {
      if (entry.getKey().matcher(nameRegex).matches()) {
        return entry.getValue();
      }
    }
    return null;
  }

  @Override
  public boolean hasCommand(String nameRegex) {
    if (commandInfoMap.containsKey(nameRegex)) {
      return true;
    }
    for (Map.Entry<Pattern, Command> entry : commands.entrySet()) {
      if (entry.getKey().matcher(nameRegex).matches()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Command getCommand(String nameRegex) {
    if (commandInfoMap.containsKey(nameRegex)) {
      return commandInfoMap.get(nameRegex);
    }
    for (Map.Entry<Pattern, Command> entry : commands.entrySet()) {
      if (entry.getKey().matcher(nameRegex).matches()) {
        return entry.getValue();
      }
    }
    return null;
  }
}
