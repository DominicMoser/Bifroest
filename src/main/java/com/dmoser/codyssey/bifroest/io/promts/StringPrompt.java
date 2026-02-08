package com.dmoser.codyssey.bifroest.io.promts;

import com.dmoser.codyssey.bifroest.io.Prompt;

public record StringPrompt(String leftValue) implements Prompt {
  @Override
  public String rightValue() {
    return null;
  }
}
