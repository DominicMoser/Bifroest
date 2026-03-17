package com.dmoser.codyssey.bifroest.io.communications;

import com.dmoser.codyssey.bifroest.io.Request;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CliRequestParser implements RequestParser<String> {

  private final Pattern fullInputPattern;
  private final Pattern argPattern;

  public CliRequestParser() {
    String pathRegex = "^(((?<origin>(/|~|\\./)?)(?<path>(\\w+/)*))?)";
    String commandRegex = " ?(?<command>\\S+)";
    String argsRegex = "(?<args>( (((-[\\w]+) (\\S)+)|(\\w+)))+)?";
    String argRegex = "((?<param>(-(?<key>[\\w]+)) (?<value>\\S+))|(?<arg>\\w+))";
    fullInputPattern = Pattern.compile(pathRegex + commandRegex + argsRegex);
    argPattern = Pattern.compile(argRegex);
  }

  @Override
  public Request parseInput(String input) {
    if (input.matches("\\.\\.\\.*")) {
      return new Request(RequestOrigin.RELATIVE, List.of(), input, List.of(), Map.of());
    }
    String[] splitInput = splitInput(input);
    String originString = splitInput[0];
    String pathString = splitInput[1];
    String commandString = splitInput[2];
    String argsString = splitInput[3];
    RequestOrigin origin =
        switch (originString) {
          case "", "./" -> RequestOrigin.RELATIVE;
          case "~" -> RequestOrigin.HOME;
          case "/" -> RequestOrigin.ROOT;
          default -> throw new IllegalStateException("Unexpected value: " + splitInput[0]);
        };

    List<String> path = pathString.isBlank() ? List.of() : List.of(pathString.split("/"));
    String command = commandString;
    List<String> args = new ArrayList<>();
    Map<String, String> params = new HashMap<>();
    parseArgs(argsString, args, params);
    return new Request(origin, path, command, args, params);
  }

  protected void parseArgs(String argString, List<String> args, Map<String, String> params) {
    Matcher argMatcher = argPattern.matcher(argString);
    while (argMatcher.find()) {
      String arg = argMatcher.group("arg");
      if (arg != null) {
        args.add(arg);
      }
      String param = argMatcher.group("param");
      if (param != null) {
        String key = argMatcher.group("key");
        String value = argMatcher.group("value");
        params.put(key, value);
      }
    }
  }

  protected String[] splitInput(String input) {
    Matcher inputMatcher = fullInputPattern.matcher(input);
    if (!inputMatcher.matches()) {
      return null;
    }
    String root = inputMatcher.group("origin");
    String path = inputMatcher.group("path");
    String command = inputMatcher.group("command");
    String args = inputMatcher.group("args");

    return new String[] {root, path, command, args == null ? "" : args.trim()};
  }
}
