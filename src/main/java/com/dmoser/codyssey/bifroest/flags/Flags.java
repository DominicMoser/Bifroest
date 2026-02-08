package com.dmoser.codyssey.bifroest.flags;

import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.io.Result;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class containing shared, reusable instances of CLI flags.
 *
 * <p>Since some {@link AbstractFlag} are stateless and their stack traces are not filled, these
 * constants can be reused across different layers and sessions to avoid unnecessary object
 * creation.
 */
public final class Flags {

  public static final Flag COMMAND_NOT_FOUND = new CommandNotFoundFlag();

  public static final Flag EXIT_FLAG = new ExitFlag();

  public static final Flag CLEAR_FLAG = new ClearFlag();

  public static NavigationFlag navigationFlag(List<String> path) {
    ArrayList<String> myPath = new ArrayList<>(path);
    return new NavigationFlag(myPath);
  }

  public static NavigationFlag navigationFlag(List<String> path, String pathEnd) {
    ArrayList<String> myPath = new ArrayList<>(path);
    myPath.add(pathEnd);
    return new NavigationFlag(myPath);
  }

  public static CommandExceptionFlag commandExceptionFlag(Exception exception) {
    return new CommandExceptionFlag(exception);
  }

  public static Result requestExceptionFlag(Exception exception) {
    return new RequestExceptionFlag(exception);
  }
}
