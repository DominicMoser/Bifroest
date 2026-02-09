package com.dmoser.codyssey.bifroest.io.promts;

import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.List;
import java.util.stream.Collectors;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class TerminalPrompt implements Prompt {
  @Override
  public String leftValue() {
    AttributedStringBuilder sb = new AttributedStringBuilder();
    List<String> path = Session.get().getCurrentPath();
    sb.append(Session.get().getName(), AttributedStyle.DEFAULT.foreground(75, 230, 255));
    sb.append(":");
    String location = path.stream().collect(Collectors.joining("/", "/", ""));
    sb.append(location, AttributedStyle.DEFAULT.foreground(75, 230, 255));
    sb.append("> ", AttributedStyle.DEFAULT.foreground(75, 230, 255));

    return sb.toAnsi();
  }

  @Override
  public String rightValue() {
    return null;
  }
}
