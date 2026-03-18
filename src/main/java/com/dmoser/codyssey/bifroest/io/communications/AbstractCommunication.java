package com.dmoser.codyssey.bifroest.io.communications;

import com.dmoser.codyssey.bifroest.io.*;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.completer.CompleterProvider;

public abstract class AbstractCommunication<INPUT_TYPE> implements Communication {
  protected final RequestParser<INPUT_TYPE> parser;

  protected AbstractCommunication(RequestParser<INPUT_TYPE> parser) {
    this.parser = parser;
  }

  @Override
  public Request getRequest(Prompt prompt) {
    INPUT_TYPE input = this.readSource(prompt);
    return parser.parseInput(input);
  }

  @Override
  public void setCompleterProvider(CompleterProvider provider) {}

  protected abstract INPUT_TYPE readSource(Prompt prompt);
}
