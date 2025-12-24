package com.dmoser.codyssey.bifroest.flags;

import java.util.List;

/** Flag used to signal that the user should be moved to a specific shell layer. */
public class ShellNavigationFlag extends AbstractFlag {

  private final List<String> target;
  private final List<String> origin;

  /**
   * Constructs a new {@code ShellNavigationFlag}.
   *
   * @param target the path to the target shell layer
   * @param origin the path to the shell layer where this flag was thrown
   */
  public ShellNavigationFlag(List<String> target, List<String> origin) {
    this.target = target;
    this.origin = origin;
  }

  /**
   * Returns the path to the shell layer where this flag was thrown.
   *
   * @return the origin layer path
   */
  public List<String> getOrigin() {
    return origin;
  }

  /**
   * Returns the path to the target shell layer.
   *
   * @return the target layer path
   */
  public List<String> getTarget() {
    return target;
  }
}
