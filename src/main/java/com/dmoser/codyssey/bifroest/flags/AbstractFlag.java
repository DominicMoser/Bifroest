package com.dmoser.codyssey.bifroest.flags;

/**
 * Base class for flags used in the CLI framework to signal messages that need to be handled by
 * another CLI layer.
 *
 * <p>Even though {@link RuntimeException}s should generally not be used for program flow control,
 * they are used here as a signaling mechanism. Since actions that throw a {@code AbstractFlag} are
 * executed after user input, the performance impact and latency are negligible.
 *
 * <p>To further minimize overhead, this class overrides {@link #fillInStackTrace()} to return
 * {@code this} without actually filling in the stack trace.
 */
public abstract class AbstractFlag extends RuntimeException {
  @Override
  public Throwable fillInStackTrace() {
    return this;
  }
}
