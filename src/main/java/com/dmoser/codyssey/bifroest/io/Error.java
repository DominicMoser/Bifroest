package com.dmoser.codyssey.bifroest.io;

import com.dmoser.codyssey.bifroest.io.errors.ErrorCode;

public record Error(int errorCode, String msg) {

  public Error(ErrorCode errorCode) {
    this(errorCode.getCode(), "");
  }

  public Error(ErrorCode errorCode, String msg) {
    this(errorCode.getCode(), msg);
  }

  public Error(int errorCode) {
    this(errorCode, "");
  }
}
