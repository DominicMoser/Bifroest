package com.dmoser.codyssey.bifroest.session;

import com.dmoser.codyssey.bifroest.BifroestBuildConfig;

public record AppConfig(String appName, String appVersion) {

  private static volatile AppConfig INSTANCE = null;

  public static AppConfig get() {

    if (INSTANCE == null) {
      // TODO create a warning initializing empty AppConfig
      INSTANCE = builder().withAppName(BifroestBuildConfig.DEFAULT_SHELL_NAME).build();
    }
    return INSTANCE;
  }

  public static SetAppNameStep builder() {
    if (INSTANCE != null) {
      throw new IllegalStateException("Already initialized");
    }
    return new AppConfigBuilder();
  }

  public interface SetAppNameStep {
    SetOptionalsStep withAppName(String appName);
  }

  public interface SetOptionalsStep {
    SetOptionalsStep andAppVersion(String appVersion);

    AppConfig build();
  }

  private static class AppConfigBuilder implements SetAppNameStep, SetOptionalsStep {
    private String appName;
    private String appVersion = BifroestBuildConfig.VERSION;

    @Override
    public SetOptionalsStep withAppName(String appName) {
      this.appName = appName;
      return this;
    }

    @Override
    public SetOptionalsStep andAppVersion(String appVersion) {
      this.appVersion = appVersion;
      return this;
    }

    @Override
    public AppConfig build() {
      INSTANCE = new AppConfig(appName, appVersion);
      return INSTANCE;
    }
  }
}
