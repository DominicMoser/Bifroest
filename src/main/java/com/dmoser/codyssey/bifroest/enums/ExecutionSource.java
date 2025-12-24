package com.dmoser.codyssey.bifroest.enums;

/**
 * Enum used to indicate the source of the last executed operation.
 *
 * <p>It helps identify whether the last command was executed as a simple standalone command or
 * through a shell layer.
 */
public enum ExecutionSource {
  /** Indicates that the execution sourced from a shell layer. */
  LAYER,
  /** Indicates that the execution sourced from a simple command. */
  COMMAND
}
