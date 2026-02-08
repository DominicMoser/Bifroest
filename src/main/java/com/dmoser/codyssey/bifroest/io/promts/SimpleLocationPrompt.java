package com.dmoser.codyssey.bifroest.io.promts;

import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.List;

public class SimpleLocationPrompt implements Prompt {
  @Override
  public String leftValue() {
    List<String> path = Session.get().getCurrentPath();
    return path.isEmpty() ? "> " : Session.get().getCurrentPath().getLast() + " > ";
  }

  @Override
  public String rightValue() {
    return null;
  }
}
