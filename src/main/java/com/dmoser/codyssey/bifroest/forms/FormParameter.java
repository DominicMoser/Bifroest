package com.dmoser.codyssey.bifroest.forms;

public record FormParameter(String name, String msg) {
  public FormParameter(String name) {
    this(name, null);
  }
}
