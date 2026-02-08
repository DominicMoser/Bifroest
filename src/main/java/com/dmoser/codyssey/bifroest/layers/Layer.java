package com.dmoser.codyssey.bifroest.layers;

import com.dmoser.codyssey.bifroest.commands.*;
import java.util.*;
import java.util.regex.Pattern;

public abstract class Layer implements NewLayer {

  private Map<Pattern, NewLayer> layers = new HashMap<>();
  private Map<String, NewLayer> layerInfoMap = new HashMap<>();

  private Map<Pattern, Command> commands = new HashMap<>();
  private Map<String, Command> commandInfoMap = new HashMap<>();

  protected Layer() {
    addCommand(new LsCommand(this));
  }

  public Set<String> getCommandNames() {
    return commandInfoMap.keySet();
  }

  @Override
  public Set<String> getLayerNames() {
    return layerInfoMap.keySet();
  }

  /*
  public Completer getCompleter() {
    return new Completers.TreeCompleter(
        Stream.concat(commandList.stream(), defaultCommandList.stream())
            .map(Command::getCompleterNode)
            .toList()
            .toArray(Completers.TreeCompleter.Node[]::new));
  }*/
  /*
  @Override
  public Completers.TreeCompleter.Node getCompleterNode() {

    List<Object> obj = new LinkedList<>();
    obj.add(this.layerName);
    obj.addAll(
        Stream.concat(commandList.stream(), defaultCommandList.stream())
            .map(Command::getCompleterNode)
            .toList());
    return node(obj.toArray());
  }*/

  public void addLayer(String name, NewLayer newLayer) {
    addLayer(name, Pattern.compile(name), newLayer);
  }

  public void addLayer(String name, Pattern layerPattern, NewLayer newLayer) {
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
    for (Map.Entry<Pattern, NewLayer> entry : layers.entrySet()) {
      if (entry.getKey().matcher(nameRegex).matches()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public NewLayer getLayer(String nameRegex) {
    for (Map.Entry<Pattern, NewLayer> entry : layers.entrySet()) {
      if (entry.getKey().matcher(nameRegex).matches()) {
        return entry.getValue();
      }
    }
    return null;
  }

  @Override
  public boolean hasCommand(String nameRegex) {
    for (Map.Entry<Pattern, Command> entry : commands.entrySet()) {
      if (entry.getKey().matcher(nameRegex).matches()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Command getCommand(String nameRegex) {
    for (Map.Entry<Pattern, Command> entry : commands.entrySet()) {
      if (entry.getKey().matcher(nameRegex).matches()) {
        return entry.getValue();
      }
    }
    return null;
  }
}
