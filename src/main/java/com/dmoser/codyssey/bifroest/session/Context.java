package com.dmoser.codyssey.bifroest.session;

import com.dmoser.codyssey.bifroest.BifroestBuildConfig;
import com.dmoser.codyssey.bifroest.banner.Banner;
import com.dmoser.codyssey.bifroest.banner.SimpleTextBanner;

public class Context {
  private static final ThreadLocal<Context> CONTEXT = ThreadLocal.withInitial(Context::new);

  private String name = BifroestBuildConfig.DEFAULT_SHELL_NAME;
  private Banner banner = new SimpleTextBanner("");

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
}
