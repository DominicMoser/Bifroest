package com.dmoser.codyssey.bifroest.io.communications;

import com.dmoser.codyssey.bifroest.io.Request;

public interface RequestParser<INPUT_TYPE> {
  Request parseInput(INPUT_TYPE input);
}
