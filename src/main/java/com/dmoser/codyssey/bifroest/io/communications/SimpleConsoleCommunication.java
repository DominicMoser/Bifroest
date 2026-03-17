package com.dmoser.codyssey.bifroest.io.communications;

import com.dmoser.codyssey.bifroest.io.Banner;
import com.dmoser.codyssey.bifroest.io.Error;
import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.Response;
import com.dmoser.codyssey.bifroest.io.completer.CompleterProvider;
import java.util.Scanner;

public class SimpleConsoleCommunication extends AbstractCommunication<String> {

  private final Scanner sc;

  public SimpleConsoleCommunication() {
    super(new CliRequestParser());
    this.sc = new Scanner(System.in);
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
    IO.println(unhandledFlag.errorCode() + " " + unhandledFlag.msg());
  }

  @Override
  public void clear() {
    IO.print("\033[H\033[2J");
  }

  @Override
  public void setCompleterProvider(CompleterProvider provider) {}

  @Override
  String readSource(Prompt prompt) {
    IO.print(prompt.leftValue());
    return sc.nextLine();
  }

  @Override
  public String requestParam(String name, String formParamMsg) {
    IO.print(name + ": ");
    return sc.nextLine();
  }
}
