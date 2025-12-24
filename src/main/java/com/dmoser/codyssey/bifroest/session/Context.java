package com.dmoser.codyssey.bifroest.session;

import com.dmoser.codyssey.bifroest.BifroestBuildConfig;
import com.dmoser.codyssey.bifroest.banner.Banner;
import com.dmoser.codyssey.bifroest.banner.SimpleTextBanner;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;

public class Context {
  private static final ThreadLocal<Context> CONTEXT = ThreadLocal.withInitial(Context::new);

  private String name = BifroestBuildConfig.DEFAULT_SHELL_NAME;
  private Banner banner = new SimpleTextBanner("");
  private LineReader lineReader = null;
  private Terminal terminal = null;
  private String version = BifroestBuildConfig.VERSION;

  public static Context get() {
    return CONTEXT.get();
  }

  public static void close() {
    CONTEXT.remove();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Banner getBanner() {
    return banner;
  }

  public void setBanner(Banner banner) {
    this.banner = banner;
  }

  public LineReader getLineReader() {
    if (lineReader == null) {
      throw new RuntimeException("line reader is not set");
    }
    return lineReader;
  }

  public void setLineReader(LineReader reader) {
    this.lineReader = reader;
  }

  public Terminal getTerminal() {
    if (terminal == null) {
      throw new RuntimeException("terminal is not set");
    }
    return terminal;
  }

  public void setTerminal(Terminal terminal) {
    this.terminal = terminal;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
}
