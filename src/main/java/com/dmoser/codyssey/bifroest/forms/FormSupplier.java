package com.dmoser.codyssey.bifroest.forms;

public interface FormSupplier<T> {
  public Form<T> create();
}
