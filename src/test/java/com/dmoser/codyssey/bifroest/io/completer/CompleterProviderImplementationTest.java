package com.dmoser.codyssey.bifroest.io.completer;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompleterProviderImplementationTest {

  @Test
  public void shouldCompleteFromRoot() {
    CompleterProviderImplementation provider = new CompleterProviderImplementation();
    provider.addPath(List.of("dice", "d2"));
    provider.addPath(List.of("dice", "d4"));
    provider.addPath(List.of("exit"));

    try {
      com.dmoser.codyssey.bifroest.session.Session.get();
    } catch (IllegalStateException e) {
      com.dmoser.codyssey.bifroest.session.Session.create()
          .withName("test")
          .andCommunication(null)
          .open();
    }
    com.dmoser.codyssey.bifroest.session.Session.get().setCurrentPath(List.of());

    List<String> completions = provider.getCompletions(List.of("d"), 0, 1);
    Assertions.assertTrue(completions.contains("dice/"));
  }

  @Test
  public void shouldCompleteFromContext() {
    CompleterProviderImplementation provider = new CompleterProviderImplementation();
    provider.addPath(List.of("dice", "d2"));
    provider.addPath(List.of("dice", "d4"));

    try {
      com.dmoser.codyssey.bifroest.session.Session.get();
    } catch (IllegalStateException e) {
      com.dmoser.codyssey.bifroest.session.Session.create()
          .withName("test")
          .andCommunication(null)
          .open();
    }
    com.dmoser.codyssey.bifroest.session.Session.get().setCurrentPath(List.of("dice"));

    List<String> completions = provider.getCompletions(List.of("d"), 0, 1);
    Assertions.assertTrue(completions.contains("d2"));
    Assertions.assertTrue(completions.contains("d4"));
  }
}
