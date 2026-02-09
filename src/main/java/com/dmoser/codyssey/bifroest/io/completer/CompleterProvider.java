package com.dmoser.codyssey.bifroest.io.completer;

import java.util.List;

@FunctionalInterface
public interface CompleterProvider {
  List<String> getCompletions(List<String> words, int wordIndex, int wordCursor);
}
