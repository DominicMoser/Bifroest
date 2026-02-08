package com.dmoser.codyssey.bifroest.flags;

import com.dmoser.codyssey.bifroest.io.Flag;
import java.util.List;

public record NavigationFlag(List<String> path) implements Flag {}
