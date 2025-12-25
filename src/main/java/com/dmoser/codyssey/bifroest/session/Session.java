package com.dmoser.codyssey.bifroest.session;

import com.dmoser.codyssey.bifroest.banner.Banner;
import java.io.PrintWriter;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;

public class Session {

  private static final ThreadLocal<Session> SESSION = new ThreadLocal<>();

  private final String name;
  private final LineReader lineReader;
  private final Banner banner;

  private Session(String name, LineReader lineReader, Banner banner) {
    this.name = name;
    this.lineReader = lineReader;
    this.banner = banner;
  }

  public static SetNameStep create() {
    Session currentSession = SESSION.get();
    if (currentSession != null) {
      throw new IllegalStateException("Session should not be created twice");
    }
    return new SessionBuilder();
  }

  public static Session get() {
    Session currentSession = SESSION.get();
    if (currentSession == null) {
      throw new IllegalStateException("Session is not created");
    }
    return SESSION.get();
  }

  public static void close() {
    SESSION.remove();
  }

  public static PrintWriter out() {
    return get().writer();
  }

  public static void logException(Exception e) {
    e.printStackTrace();
  }

  public PrintWriter writer() {
    return Session.get().getTerminal().writer();
  }

  public Banner getBanner() {
    return banner;
  }

  public LineReader getLineReader() {
    return lineReader;
  }

  public Terminal getTerminal() {
    return lineReader.getTerminal();
  }

  public String getName() {
    return name;
  }

  public interface SetNameStep {
    SetLineReaderStep withName(String name);
  }

  public interface SetBannerStep {
    SetOptionalsStep andBanner(Banner banner);
  }

  public interface SetLineReaderStep {
    SetBannerStep andLineReader(LineReader lineReader);
  }

  public interface SetOptionalsStep {
    Session open();
  }

  public static class SessionBuilder
      implements SetNameStep, SetBannerStep, SetLineReaderStep, SetOptionalsStep {
    String name;
    Banner banner;
    LineReader lineReader;

    @Override
    public SetOptionalsStep andBanner(Banner banner) {
      this.banner = banner;
      return this;
    }

    @Override
    public SetBannerStep andLineReader(LineReader lineReader) {
      this.lineReader = lineReader;
      return this;
    }

    @Override
    public SetLineReaderStep withName(String name) {
      this.name = name;
      return this;
    }

    @Override
    public Session open() {
      Session newSession = new Session(name, lineReader, banner);
      SESSION.set(newSession);
      return Session.get();
    }
  }
}
