package com.dmoser.codyssey.bifroest.layers;

import static org.jline.builtins.Completers.TreeCompleter.node;

import com.dmoser.codyssey.bifroest.commands.*;
import com.dmoser.codyssey.bifroest.enums.ExecutionSource;
import com.dmoser.codyssey.bifroest.flags.ShellExitFlag;
import com.dmoser.codyssey.bifroest.session.Context;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.jline.builtins.Completers;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public abstract class Layer implements Command {

  protected final String layerName;
  protected List<Command> defaultCommandList = new ArrayList<>();
  protected List<Command> commandList = new ArrayList<>();
  protected String parentName = "";

  protected Layer(String layerName) {
    this.layerName = layerName;
    defaultCommandList.add(new ExitCommand());
    defaultCommandList.add(new NavigationCommand());
    defaultCommandList.add(new UpCommand());
    defaultCommandList.add(new ClearCommand());
    defaultCommandList.add(new LsCommand());
  }

  public List<String> getCommandNames() {
    return commandList.stream().map(Command::getName).toList();
  }

  public String generatePrompt() {
    AttributedStringBuilder sb = new AttributedStringBuilder();
    String location = getLocation();
    if (!location.startsWith("/")) {
      location = "/" + location;
    }
    sb.append(Context.get().getName(), AttributedStyle.DEFAULT.foreground(75, 230, 255));
    sb.append(":");
    sb.append(location, AttributedStyle.DEFAULT.foreground(75, 230, 255));
    sb.append("> ", AttributedStyle.DEFAULT.foreground(75, 230, 255));

    return sb.toAnsi();
  }

  public String getLocation() {
    return parentName + "/" + layerName;
  }

  @Override
  public String getNameRegex() {
    return "^" + layerName + "$";
  }

  public Completer getCompleter() {
    return new Completers.TreeCompleter(
        Stream.concat(commandList.stream(), defaultCommandList.stream())
            .map(Command::getCompleterNode)
            .toList()
            .toArray(Completers.TreeCompleter.Node[]::new));
  }

  @Override
  public Completers.TreeCompleter.Node getCompleterNode() {

    List<Object> obj = new LinkedList<>();
    obj.add(this.layerName);
    obj.addAll(
        Stream.concat(commandList.stream(), defaultCommandList.stream())
            .map(Command::getCompleterNode)
            .toList());
    return node(obj.toArray());
  }

  public void addCommand(Command command) {
    commandList.add(command);
  }

  public void addCommand(String name, SimpleCommand simpleCommand) {
    commandList.add(new SimpleCommandContainer(name, simpleCommand));
  }

  public void enterLayer() {

    Completer oldCompleter = null;
    if (Context.get().getLineReader() instanceof LineReaderImpl lineReaderImpl) {
      oldCompleter = lineReaderImpl.getCompleter();
      lineReaderImpl.setCompleter(getCompleter());
    }
    LineReader lineReader = Context.get().getLineReader();
    while (true) {
      try {
        String line =
            lineReader.readLine(
                new AttributedStringBuilder()
                    .ansiAppend(generatePrompt())
                    .toAnsi(lineReader.getTerminal()));
        var commandList = new ArrayList<>(lineReader.getParsedLine().words());
        if (commandList.size() > 1 && commandList.getLast().isEmpty()) {
          commandList.removeLast();
        }
        executeCommand(commandList, true);
      } catch (UserInterruptException e) {
      } catch (ShellExitFlag e) {
        break;
      }
    }
    if (lineReader instanceof LineReaderImpl lineReaderImpl) {
      lineReaderImpl.setCompleter(oldCompleter);
    }
  }

  @Override
  public ExecutionSource execute(Layer parent, List<String> command) {
    parentName = parent == null ? "" : parent.getLocation();
    if (command.isEmpty()) {
      throw new RuntimeException("Command should never be null");
    }

    // When the calling command does not match the name regex, then something weird happended.
    if (!(command.getFirst().matches(getNameRegex()))) {
      throw new RuntimeException("A command should always be called by a qualified name");
    }

    // When the command size is only 1 that means that this shell is called.
    command.removeFirst();

    if (command.isEmpty() || executeCommand(command, false) == ExecutionSource.LAYER) {
      enterLayer();
      return ExecutionSource.LAYER;
    }
    return ExecutionSource.COMMAND;
  }

  private boolean matchKey(String key, String command) {
    Pattern p = Pattern.compile(key);
    return p.matcher(command).matches();
  }

  protected ExecutionSource executeCommand(List<String> command, boolean calledInShell) {

    ExecutionSource shellReturnType =
        calledInShell ? ExecutionSource.LAYER : ExecutionSource.COMMAND;

    // Look for matching command.

    Optional<Command> commandOptional =
        commandList.stream()
            .filter(cmd -> matchKey(cmd.getNameRegex(), command.getFirst().trim()))
            .findFirst();

    if (commandOptional.isEmpty()) {
      commandOptional =
          defaultCommandList.stream()
              .filter(cmd -> matchKey(cmd.getNameRegex(), command.getFirst().trim()))
              .findFirst();
    }

    // No command found. Maybe return something
    if (commandOptional.isEmpty()) {
      Context.get().getTerminal().writer().println("Command Not Found");
      return shellReturnType;
    }

    Command shellCommand = commandOptional.get();

    ExecutionSource commandReturnType = shellCommand.execute(this, command);

    if (shellReturnType == ExecutionSource.LAYER) {
      return ExecutionSource.LAYER;
    }
    return commandReturnType;
  }

  protected boolean checkForPath(List<String> path) {
    // We are in this one.
    if (path.isEmpty()) {
      return true;
    }
    path = new ArrayList<>(path);

    String nextLayer = path.getFirst();
    path.removeFirst();

    List<String> finalPath = path;
    return commandList.stream()
        .filter(c -> c instanceof Layer)
        .map(s -> (Layer) s)
        .filter(s -> matchKey(s.getNameRegex(), nextLayer))
        .map(s -> s.checkForPath(finalPath))
        .findFirst()
        .orElse(false);
  }

  public String getName() {
    return this.layerName;
  }
}
