package com.dmoser.codyssey.bifroest.io;

public record Error(int errorCode, String msg) {
  public static final int ERROR_UNHANDLED = 123;

  public Error(int errorCode) {
    this(errorCode, "");
  }
}
