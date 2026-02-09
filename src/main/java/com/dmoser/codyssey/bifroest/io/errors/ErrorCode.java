package com.dmoser.codyssey.bifroest.io.errors;

public enum ErrorCode {
  // Generic errors
  UNKNOWN(1),
  INTERNAL_ERROR(2),
  NOT_IMPLEMENTED(3),

  // Resource/Input errors (POSIX-like)
  NOT_FOUND(404),
  PERMISSION_DENIED(13),
  IO_ERROR(5),
  ALREADY_EXISTS(17),
  INVALID_ARGUMENT(22),
  INVALID_INPUT(23),

  // Application/CLI specific
  UNHANDLED_FLAG(123),
  COMMAND_NOT_FOUND(127),
  TIMEOUT(110);

  private final int code;

  ErrorCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
