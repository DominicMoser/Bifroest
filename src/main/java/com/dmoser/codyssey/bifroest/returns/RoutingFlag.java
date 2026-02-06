package com.dmoser.codyssey.bifroest.returns;

/**
 * Enum used to indicate the navigation behavior after a command execution.
 *
 * <p>This flag determines whether the shell should stay in the current (deepest) layer or return to
 * the layer where the command was originally called from. It is used as a return value for command
 * executions to control the shell's navigation flow.
 */
public enum RoutingFlag {
  /**
   * Indicates that the user should remain in the current (deepest) layer after execution.
   *
   * <p>For example, if a command enters a new sub-layer, the shell will stay in that sub-layer and
   * start an interactive loop there.
   */
  REMAIN,

  /**
   * Indicates that the user should return to the layer where the command was originally called
   * from.
   *
   * <p>For example, if you are in "root/" and execute "layer1/command1", and the command returns
   * {@code RETURN}, you will be directed back to "root/".
   */
  RETURN
}
