package com.dmoser.codyssey.bifroest.flags;

import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.layers.NewLayer;

public record ListCommandsFlag(NewLayer layer) implements Flag {}
