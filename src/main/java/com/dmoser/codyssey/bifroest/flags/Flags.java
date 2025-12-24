package com.dmoser.codyssey.bifroest.flags;

/**
 * Utility class containing shared, reusable instances of CLI flags.
 *
 * <p>Since some {@link AbstractFlag} are stateless and their stack traces are not filled, these
 * constants can be reused across different layers and sessions to avoid unnecessary object
 * creation.
 */
public final class Flags {
  /** A shared instance of {@link ShellExitFlag} used to signal exiting one shell layer. */
  public static final AbstractFlag SHELL_EXIT_FLAG = new ShellExitFlag();

  /** A shared instance of {@link SystemExitFlag} used to signal stopping the client. */
  public static final AbstractFlag SYSTEM_EXIT_FLAG = new SystemExitFlag();
}
