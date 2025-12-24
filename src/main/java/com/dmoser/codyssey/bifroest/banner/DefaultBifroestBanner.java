package com.dmoser.codyssey.bifroest.banner;

import com.dmoser.codyssey.bifroest.session.Context;
import java.util.regex.Pattern;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class DefaultBifroestBanner implements Banner {

  String bannerText =
      """
      ░░░░░░░░░░░░░░░░░        ____  _ ____                     __
      ░░░░░░░░███░░░░░░       / __ )(_) __/________  ___  _____/ /_
      ░░░░░░░░█░███░░░░      / __  / / /_/ ___/ __ \\/ _ \\/ ___/ __/
      ░░░░░░░░█░░░░░░░░     / /_/ / / __/ /  / /_/ /  __(__  ) /_
      ░░░░░░░░███░░░░░░    /_____/_/_/ /_/   \\____/\\___/____/\\__/
      ░░░░░░░░█░███░░░░
      ░░░░░░░░█░░░░░░░░   ================================================================
      ░░░░░███████░░░░░   %s (%s)
      ░░░███░░█░░███░░░   A CLI Server with ssh capabilities.
      ░███░░░░█░░░░███░
      ░░░███░░█░░███░░░
      ░███░░░░█░░░░███░
      ░░░░░░░░█░░███░░░
      ░░░░░░░░████░░░░░
      ██░░░░░░█░░░░░░██
      ░██████░█░██████░
      ░░░░░░█████░░░░░░
      ░██████░█░██████░
      ██░░░░░░█░░░░░░██
      ░░░░░░░░█░░░░░░░░
      ░░░░░░░░░░░░░░░░░
      """;

  @Override
  public String getString() {
    AttributedStringBuilder sb = new AttributedStringBuilder();
    var blockStyle =
        AttributedStyle.DEFAULT.foreground(75, 230, 255).background(AttributedStyle.RED);
    var fontStyle = AttributedStyle.DEFAULT.foreground(75, 230, 255);
    sb.append(bannerText.formatted(Context.get().getName(), Context.get().getVersion()))
        .styleMatches(Pattern.compile("█"), blockStyle)
        .styleMatches(Pattern.compile("(?<![a-zA-Z0-9])[\\/\\\\_(),`](?![a-zA-Z0-9])"), fontStyle);
    return sb.toAnsi().replace("\n", "\n\r");
  }
}
