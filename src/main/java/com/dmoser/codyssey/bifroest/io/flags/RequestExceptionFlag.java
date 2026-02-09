package com.dmoser.codyssey.bifroest.io.flags;

import com.dmoser.codyssey.bifroest.io.errors.ErrorCode;

public record RequestExceptionFlag(Exception exception, ErrorCode errorCode, String errorMsg)
    implements ExceptionFlag {
  public RequestExceptionFlag(Exception exception, ErrorCode errorCode) {
    this(exception, errorCode, "");
  }
}
