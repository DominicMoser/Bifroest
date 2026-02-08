package com.dmoser.codyssey.bifroest.io;

public non-sealed interface Flag extends Result {

  static final Flag ERROR_FLAG = new ErrorFlag();

  record ErrorFlag() implements Flag {}
}
