package com.dmoser.codyssey.bifroest.banner;

import com.dmoser.codyssey.bifroest.session.Context;

public class SimpleContextNameBanner implements Banner {

  public SimpleContextNameBanner() {}

  @Override
  public String getString() {
    return Context.get().getName();
  }
}
