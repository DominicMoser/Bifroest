package com.dmoser.codyssey.bifroest;

import com.dmoser.codyssey.bifroest.banner.DefaultBifroestBanner;
import com.dmoser.codyssey.bifroest.example.customers.CustomerLayer;
import com.dmoser.codyssey.bifroest.io.promts.TerminalPrompt;
import com.dmoser.codyssey.bifroest.layers.Layer;
import com.dmoser.codyssey.bifroest.runners.TerminalRunner;
import com.dmoser.codyssey.bifroest.session.AppConfig;
import java.util.Random;

/**
 * An example class that demonstrates how to use the Bifroest library to start an SSH CLI server.
 *
 * <p>This class contains a {@code main} method that initializes and starts an example SSH server,
 * providing a command-line interface for remote interaction.
 */
public class Example extends Layer {

  public Example() {
    super();
    addLayer("dice", new Dice());
    addLayer("customer", new CustomerLayer());

    // addLayer(new CustomerLayer());
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
            .andPrompt(new TerminalPrompt())
            .build();

    runner.run();
  }

  private static class Dice extends Layer {

    protected Dice() {
      super();
      addLayer("t ", new CustomerLayer());
      addLayer("deep", this);
      addCommand("d2", (args) -> new Random().nextInt(1, 3));
      addCommand("d4", (args) -> new Random().nextInt(1, 5));
      addCommand("d6", (args) -> new Random().nextInt(1, 7));
      addCommand("d10", (args) -> new Random().nextInt(1, 11));
      addCommand("d100", (args) -> new Random().nextInt(1, 101));
      addCommand("r", (args) -> new Random().nextInt(1, Integer.parseInt(args.getFirst())));
    }
  }
}
