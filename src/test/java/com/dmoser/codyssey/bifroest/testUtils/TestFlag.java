package com.dmoser.codyssey.bifroest.testUtils;

import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.io.Request;

public record TestFlag(String val, Request request) implements Flag {}
