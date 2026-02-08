package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.flags.Flags;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * Command used to navigate between different shell layers.
 *
 * <p>This command matches path-like patterns and triggers navigation by throwing with the
 * calculated target and origin paths.
 */
public class NavigationCommand implements ComplexCommand {

  // MERGE with NavigationCommand
  private static int countDots(String input) {
    int count = 0;
    for (int i = 0; i < input.length(); i++) {
      if (input.charAt(i) == '.') {
        count++;
      }
    }
    return count;
  }

  @Override
  public String getName() {
    return "..";
  }

  @Override
  public String getRegex() {
    String segment = "(?!\\.\\.?(/|$))[\\w. ]+";
    return "^(/"
        + segment
        + ")?((/"
        + segment
        + ")+)/?$|^("
        + segment
        + "/)$|^("
        + segment
        + "(/"
        + segment
        + ")+/?)$|^(\\.\\.(/\\.\\.)*(/"
        + segment
        + ")*)/?$|^(\\.\\.\\.+)(/"
        + segment
        + ")*$|^(/)$";
  }

  @Override
  public Result execute(Request request) {
    String rawPath = request.getCommand();
    List<String> currentPath = Session.get().getCurrentPath();
    List<String> splitPathSelector = splitString(rawPath);

    List<String> newPathSegments = new ArrayList<>();

    int startIndex = 0;
    if (!splitPathSelector.isEmpty() && splitPathSelector.get(0).equals("/")) {
      startIndex = 1;
    } else {
      newPathSegments.addAll(currentPath);
    }

    for (int i = startIndex; i < splitPathSelector.size(); i++) {
      String segment = splitPathSelector.get(i);
      if (segment.equals("..")) {
        if (!newPathSegments.isEmpty()) {
          newPathSegments.remove(newPathSegments.size() - 1);
        }
      } else {
        newPathSegments.add(segment);
      }
    }

    return Flags.navigationFlag(newPathSegments);
  }

  public List<String> splitString(String rawPath) {
    List<String> result = new ArrayList<>();
    if (rawPath.startsWith("/")) {
      result.add("/");
    }

    String[] parts = rawPath.split("/");
    for (String part : parts) {
      if (part.isEmpty()) {
        continue;
      }

      if (part.matches("\\.\\.\\.+")) {
        int dots = part.length();
        for (int i = 0; i < dots - 1; i++) {
          result.add("..");
        }
      } else {
        result.add(part);
      }
    }

    return result;
  }
}
