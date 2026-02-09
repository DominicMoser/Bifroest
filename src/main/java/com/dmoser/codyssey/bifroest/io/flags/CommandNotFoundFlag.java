package com.dmoser.codyssey.bifroest.io.flags;

import com.dmoser.codyssey.bifroest.io.errors.ErrorCode;

public record CommandNotFoundFlag(ErrorCode errorCode, String errorMsg) implements ErrorFlag {}
