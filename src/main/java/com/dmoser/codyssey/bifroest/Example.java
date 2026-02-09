package com.dmoser.codyssey.bifroest;

import com.dmoser.codyssey.bifroest.app.BifroestApp;
import com.dmoser.codyssey.bifroest.app.BifroestCliApp;
import com.dmoser.codyssey.bifroest.example.customers.CustomerLayer;
import com.dmoser.codyssey.bifroest.io.banners.DefaultBifroestBanner;
import com.dmoser.codyssey.bifroest.io.communications.SimpleConsoleCommunication;
import com.dmoser.codyssey.bifroest.io.promts.TerminalPrompt;
import com.dmoser.codyssey.bifroest.session.AppConfig;
import com.dmoser.codyssey.bifroest.structure.AbstractLayer;
import java.util.Random;

/**
 * An example class that demonstrates how to use the Bifroest library to start an SSH CLI server.
 *
 * <p>This class contains a {@code main} method that initializes and starts an example SSH server,
 * providing a command-line interface for remote interaction.
 */
public class Example extends AbstractLayer {

  public Example() {
    super();
    addLayer("dice", new Dice());
    addLayer("customer", new CustomerLayer());
  }

  /** Main entry point for the example application. Starts the SSH CLI server. */
  static void main() {
    AppConfig.builder().withAppName("Bifroest").andAppVersion("1.0.0").build();
    Example example = new Example();

    BifroestCliApp app =
        BifroestApp.builder()
            .cli()
            .withName("Bifroest")
            .andCommunication(new SimpleConsoleCommunication())
            .andEntryPoint(example)
            .andBanner(new DefaultBifroestBanner())
            .andPrompt(new TerminalPrompt())
            .build();

    app.run();
  }

  private static class Dice extends AbstractLayer {

    protected Dice() {
      super();
      addLayer("t ", new CustomerLayer());
      addLayer("deep", this);
      addCommand("d2", (p, args) -> new Random().nextInt(1, 3));
      addCommand("d4", (p, args) -> new Random().nextInt(1, 5));
      addCommand("d6", (p, args) -> new Random().nextInt(1, 7));
      addCommand("d10", (p, args) -> new Random().nextInt(1, 11));
      addCommand("d100", (p, args) -> new Random().nextInt(1, 101));
      addCommand("r", (p, args) -> new Random().nextInt(1, Integer.parseInt(args.getFirst())));
    }
  }
}
