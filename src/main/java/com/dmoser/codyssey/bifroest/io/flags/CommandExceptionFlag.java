package com.dmoser.codyssey.bifroest.io.flags;

import com.dmoser.codyssey.bifroest.io.errors.ErrorCode;

public record CommandExceptionFlag(Exception exception, ErrorCode errorCode, String errorMsg)
    implements ExceptionFlag {
  public CommandExceptionFlag(Exception exception, ErrorCode errorCode) {
    this(exception, errorCode, "");
  }
}
