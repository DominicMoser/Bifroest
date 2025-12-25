package com.dmoser.codyssey.bifroest;

import com.dmoser.codyssey.bifroest.banner.DefaultBifroestBanner;
import com.dmoser.codyssey.bifroest.layers.Layer;
import com.dmoser.codyssey.bifroest.layers.RootShell;
import com.dmoser.codyssey.bifroest.runners.TerminalRunner;
import com.dmoser.codyssey.bifroest.session.AppConfig;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.Random;

/**
 * An example class that demonstrates how to use the Bifroest library to start an SSH CLI server.
 *
 * <p>This class contains a {@code main} method that initializes and starts an example SSH server,
 * providing a command-line interface for remote interaction.
 */
public class Example extends RootShell {

  public Example() {
    super();
    addCommand(new Dice());
  }

  /** Main entry point for the example application. Starts the SSH CLI server. */
  static void main() {
    AppConfig.builder().withAppName("Bifroest").andAppVersion("1.0.0").build();
    Example example = new Example();

    TerminalRunner runner =
        TerminalRunner.builder()
            .withName("Bifroest")
            .andRootShell(example)
            .andBanner(new DefaultBifroestBanner())
            .build();
    runner.run();
  }

  private static class Dice extends Layer {

    protected Dice() {
      super("dice");

      addCommand("d2", (params) -> Session.out().println(new Random().nextInt(1, 3)));
      addCommand("d4", (params) -> Session.out().println(new Random().nextInt(1, 5)));
      addCommand("d6", (params) -> Session.out().println(new Random().nextInt(1, 7)));
      addCommand("d10", (params) -> Session.out().println(new Random().nextInt(1, 11)));
      addCommand("d100", (params) -> Session.out().println(new Random().nextInt(1, 101)));
      addCommand(
          "r",
          (params) -> {
            params.forEach(p -> Session.out().println(p));
            Session.out().println(new Random().nextInt(1, Integer.parseInt(params.getLast())));
          });
    }
  }
}
