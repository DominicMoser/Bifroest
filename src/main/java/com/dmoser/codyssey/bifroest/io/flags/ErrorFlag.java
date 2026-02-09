package com.dmoser.codyssey.bifroest.io.flags;

import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.io.errors.ErrorCode;

public interface ErrorFlag extends Flag {
  ErrorCode errorCode();

  String errorMsg();
}
