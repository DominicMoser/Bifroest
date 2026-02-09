package com.dmoser.codyssey.bifroest.io;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Request {
  private final List<String> origin;
  private final List<String> inputString;
  private final int size;
  private int pointer = 0;

  private Request(List<String> origin, List<String> inputString) {
    this.origin = origin;
    this.inputString = inputString;
    this.size = inputString.size();
  }

  public static Request of(List<String> origin, List<String> inputString) {
    if (inputString == null || inputString.isEmpty()) {
      throw new IllegalStateException();
    }
    if (origin == null) {
      origin = List.of();
    }

    return new Request(List.copyOf(origin), List.copyOf(inputString));
  }

  public static Request of(List<String> origin, String inputString) {
    if (inputString == null) {
      throw new IllegalStateException();
    }
    return of(origin, parseInputString(inputString));
  }

  public static List<String> parseInputString(String inputString) {
    List<String> result = new ArrayList<>();
    // TODO put outside
    Matcher matcher = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(inputString);

    while (matcher.find()) {
      if (matcher.group(1) != null) {
        result.add(matcher.group(1)); // quoted text (without quotes)
      } else {
        result.add(matcher.group(2)); // normal word
      }
    }
    return result;
  }

  public List<String> getArguments() {
    return inputString.subList(pointer + 1, inputString.size());
  }

  public String getCommand() {
    return inputString.get(pointer);
  }

  public List<String> getPath() {
    List<String> returnList = new ArrayList<>(origin);
    returnList.addAll(inputString.subList(0, pointer));
    return returnList;
  }

  public boolean movePointer() {
    if (pointer + 1 == size) {
      return false;
    }
    pointer++;
    return true;
  }

  public void resetPointer() {
    pointer = 0;
  }

  @Override
  public String toString() {
    return (getPath().isEmpty()
            ? ""
            : getPath().stream().collect(Collectors.joining("/", "(", ")")) + " ")
        + getCommand()
        + " "
        + getArguments().toString();
  }

  public List<String> getOrigin() {
    return origin;
  }
}
