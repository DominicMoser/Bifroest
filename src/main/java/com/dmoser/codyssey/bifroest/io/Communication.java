package com.dmoser.codyssey.bifroest.io;

import com.dmoser.codyssey.bifroest.io.completer.CompleterProvider;

public interface Communication {
  Request getRequest(Prompt prompt);

  void printResponse(Response result);

  void printBanner(Banner banner);

  void close();

  void printError(Error unhandledFlag);

  void clear();

  void setCompleterProvider(CompleterProvider provider);

  String getParam(String name, String formParamMsg);
}
