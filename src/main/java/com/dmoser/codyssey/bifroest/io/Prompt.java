package com.dmoser.codyssey.bifroest.io;

import com.dmoser.codyssey.bifroest.io.promts.StringPrompt;

public interface Prompt {
  Prompt DEFAULT = new StringPrompt("> ");

  String leftValue();

  String rightValue();
}
