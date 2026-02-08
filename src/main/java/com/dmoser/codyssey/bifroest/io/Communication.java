package com.dmoser.codyssey.bifroest.io;

import com.dmoser.codyssey.bifroest.banner.Banner;
import com.dmoser.codyssey.bifroest.io.completer.Root;

public interface Communication {
  Request getRequest(Prompt prompt);

  void printResponse(Response result);

  void printBanner(Banner banner);

  void close();

  void printError(Error unhandledFlag);

  void clear();

  void setCompleter(Root root);
}
