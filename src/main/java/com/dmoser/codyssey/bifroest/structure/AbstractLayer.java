package com.dmoser.codyssey.bifroest.structure;

import com.dmoser.codyssey.bifroest.session.Session;
import java.util.*;
import java.util.regex.Pattern;

public abstract class AbstractLayer implements Layer {

  private final Map<Pattern, Layer> layers = new HashMap<>();
  private final Map<String, Layer> layerInfoMap = new HashMap<>();

  private final Map<Pattern, Command> commands = new HashMap<>();
  private final Map<String, Command> commandInfoMap = new HashMap<>();
  private final String layerUuid = UUID.randomUUID().toString();

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

  @Override
  public String getLayerUUID() {
    return this.layerUuid;
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
  public Layer getLayer(String nameValue) {
    if (layerInfoMap.containsKey(nameValue)) {
      return layerInfoMap.get(nameValue);
    }
    for (Map.Entry<Pattern, Layer> entry : layers.entrySet()) {
      if (entry.getKey().matcher(nameValue).matches()) {
        Layer regexLayer = entry.getValue();

        Session.get().putVariable(regexLayer.getLayerUUID() + "invoke", nameValue);
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
