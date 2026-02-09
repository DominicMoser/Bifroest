package com.dmoser.codyssey.bifroest.io.communications;

import com.dmoser.codyssey.bifroest.io.Banner;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Error;
import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Response;
import com.dmoser.codyssey.bifroest.io.completer.CompleterProvider;
import com.dmoser.codyssey.bifroest.session.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

public class JLineSSHCommunication implements Communication {
  private final Terminal terminal;
  private final LineReader lineReader;

  public JLineSSHCommunication(InputStream in, OutputStream out) {
    try {
      System.setProperty("org.jline.terminal.jansi", "false");
      this.terminal = TerminalBuilder.builder().system(false).streams(in, out).dumb(false).build();
      this.lineReader = createLineReader();
      terminal.puts(InfoCmp.Capability.clear_screen);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Request getRequest(Prompt prompt) {
    String input = lineReader.readLine(prompt.leftValue(), "", (Character) null, "");
    return Request.of(Session.get().getCurrentPath(), input);
  }

  @Override
  public void printResponse(Response response) {
    terminal.writer().println(response.getValue());
    terminal.flush();
  }

  @Override
  public void printBanner(Banner banner) {
    terminal.writer().println(banner.getString());
    terminal.flush();
  }

  @Override
  public void close() {
    try {
      terminal.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void printError(Error error) {
    terminal.writer().println(error.errorCode() + ": " + error.msg());
  }

  @Override
  public void clear() {
    terminal.puts(InfoCmp.Capability.clear_screen);
    terminal.flush();
  }

  @Override
  public void setCompleterProvider(CompleterProvider provider) {
    if (this.lineReader instanceof LineReaderImpl lineReaderImpl) {
      lineReaderImpl.setCompleter(
          (reader, line, candidates) -> {
            List<String> completions =
                provider.getCompletions(line.words(), line.wordIndex(), line.wordCursor());
            for (String completion : completions) {
              candidates.add(new Candidate(completion));
            }
          });
    }
  }

  @Override
  public String getParam(String name, String formParamMsg) {
    String prompt = formParamMsg != null ? formParamMsg : "Please enter %s> ".formatted(name);
    return lineReader.readLine(prompt);
  }

  private LineReader createLineReader() {
    return LineReaderBuilder.builder()
        .terminal(terminal)
        .variable(LineReader.LIST_MAX, 50) // max tab completion candidates
        .completer(
            new ArgumentCompleter(new StringsCompleter("exit", "help"), NullCompleter.INSTANCE))
        .option(LineReader.Option.AUTO_LIST, true) // Automatically list options
        .option(LineReader.Option.LIST_PACKED, true) // Display completions in a compact form
        .option(LineReader.Option.AUTO_MENU, true) // Show menu automatically
        .option(LineReader.Option.MENU_COMPLETE, true) // Cycle through completions
        .build();
  }
}
