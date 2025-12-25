package com.dmoser.codyssey.bifroest.runners;

import com.dmoser.codyssey.bifroest.banner.Banner;
import com.dmoser.codyssey.bifroest.layers.RootShell;
import com.dmoser.codyssey.bifroest.session.Session;
import java.io.IOException;
import org.jline.reader.LineReader;
import org.jline.utils.InfoCmp;

public abstract class AbstractRunner {

  public void run() {
    init();
    start();
    stop();
  }

  protected void init() {
    Session.create()
        .withName(getName())
        .andLineReader(getLineReader())
        .andBanner(getBanner())
        .open();
    System.setProperty("org.jline.terminal.jansi", "false");
  }

  protected void start() {
    Session.get().getTerminal().puts(InfoCmp.Capability.clear_screen);
    Session.out().println(Session.get().getBanner().getString());
    Session.get().getTerminal().flush();
    getRootShell().start();
  }

  protected void stop() {
    try {
      Session.get().getTerminal().close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Session.close();
  }

  protected abstract RootShell getRootShell();

  protected abstract LineReader getLineReader();

  protected abstract Banner getBanner();

  protected abstract String getName();
}
