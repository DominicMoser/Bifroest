package com.dmoser.codyssey.bifroest.io.communications;

import com.dmoser.codyssey.bifroest.io.Banner;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Error;
import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Response;
import com.dmoser.codyssey.bifroest.io.completer.CompleterProvider;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.Scanner;

public class SimpleConsoleCommunication implements Communication {

  private final Scanner sc;

  public SimpleConsoleCommunication() {
    this.sc = new Scanner(System.in);
  }

  @Override
  public Request getRequest(Prompt prompt) {
    IO.print(prompt.leftValue());
    return Request.of(Session.get().getCurrentPath(), sc.nextLine());
  }

  @Override
  public void printResponse(Response result) {
    IO.println(result.getValue());
  }

  @Override
  public void printBanner(Banner banner) {
    IO.println(banner.getString());
  }

  @Override
  public void close() {}

  @Override
  public void printError(Error unhandledFlag) {
    System.err.println(unhandledFlag.errorCode() + " " + unhandledFlag.msg());
  }

  @Override
  public void clear() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  @Override
  public void setCompleterProvider(CompleterProvider provider) {}

  @Override
  public String getParam(String name, String formParamMsg) {
    IO.print(name + ": ");
    return sc.nextLine();
  }
}
