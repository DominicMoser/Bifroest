package com.dmoser.codyssey.bifroest;

import com.dmoser.codyssey.bifroest.banner.DefaultBifroestBanner;
import com.dmoser.codyssey.bifroest.runners.TerminalRunner;

/**
 * An example class that demonstrates how to use the Bifroest library to start an SSH CLI server.
 *
 * <p>This class contains a {@code main} method that initializes and starts an example SSH server,
 * providing a command-line interface for remote interaction.
 */
public class Example {
  /** Main entry point for the example application. Starts the SSH CLI server. */
  static void main() {
    TerminalRunner runner = new TerminalRunner("bleb", new DefaultBifroestBanner());
    runner.start();
  }
}
