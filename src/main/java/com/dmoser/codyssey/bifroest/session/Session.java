package com.dmoser.codyssey.bifroest.session;

import com.dmoser.codyssey.bifroest.banner.Banner;
import com.dmoser.codyssey.bifroest.forms.Form;
import com.dmoser.codyssey.bifroest.forms.FormReader;
import com.dmoser.codyssey.bifroest.forms.LineReaderFormReader;
import com.dmoser.codyssey.bifroest.io.Communication;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;

public class Session {

  private static final ThreadLocal<Session> SESSION = new ThreadLocal<>();

  private final String name;
  private final LineReader lineReader;
  private final Banner banner;
  private final FormReader formReader;
  private final Communication io;
  List<String> currentPath = new ArrayList<>();
  private boolean isRunning = true;

  private Session(String name, Communication communication, Banner banner, FormReader formReader) {
    this.name = name;
    this.lineReader = null;
    this.banner = banner;
    this.formReader = formReader;
    this.io = communication;
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
    io().close();
    SESSION.remove();
  }

  public static PrintWriter out() {
    return get().writer();
  }

  public static Communication io() {
    return get().getIO();
  }

  public static void logException(Exception e) {
    e.printStackTrace();
  }

  public static Object submitForm(Form<?> form) {
    Map<String, String> formElements = get().formReader.fillFormElements(form.getFormParameters());
    return form.submit(formElements);
  }

  public static LineReader in() {
    return get().lineReader;
  }

  public Communication getIO() {
    return io;
  }

  // public LineReader getLineReader() {
  //    return lineReader;
  // }

  public PrintWriter writer() {
    return Session.get().getTerminal().writer();
  }

  public Banner getBanner() {
    return banner;
  }

  public Terminal getTerminal() {
    return lineReader.getTerminal();
  }

  public String getName() {
    return name;
  }

  public LineReader getLineReader() {
    return null;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void stop() {
    this.isRunning = false;
  }

  public List<String> getCurrentPath() {
    return currentPath;
  }

  public void setCurrentPath(List<String> newPath) {
    this.currentPath = newPath;
  }

  public interface SetNameStep {
    SetCommunication withName(String name);
  }

  public interface SetBannerStep {
    SetOptionalsStep andBanner(Banner banner);
  }

  public interface SetCommunication {
    SetBannerStep andCommunication(Communication communication);
  }

  public interface SetOptionalsStep {
    SetOptionalsStep andFormReader(FormReader formReader);

    Session open();
  }

  public static class SessionBuilder
      implements SetNameStep, SetBannerStep, SetCommunication, SetOptionalsStep {
    String name;
    Banner banner;
    Communication communication;
    FormReader formReader = new LineReaderFormReader();

    @Override
    public SetOptionalsStep andBanner(Banner banner) {
      this.banner = banner;
      return this;
    }

    @Override
    public SetBannerStep andCommunication(Communication communication) {
      this.communication = communication;
      return this;
    }

    @Override
    public SetCommunication withName(String name) {
      this.name = name;
      return this;
    }

    @Override
    public SetOptionalsStep andFormReader(FormReader formReader) {
      this.formReader = formReader;
      return this;
    }

    @Override
    public Session open() {
      Session newSession = new Session(name, communication, banner, formReader);
      SESSION.set(newSession);
      return Session.get();
    }
  }
}
