package com.dmoser.codyssey.bifroest.app;

import com.dmoser.codyssey.bifroest.Example;
import com.dmoser.codyssey.bifroest.io.Banner;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.banners.SimpleContextNameBanner;
import com.dmoser.codyssey.bifroest.io.communications.JLineSSHCommunication;
import com.dmoser.codyssey.bifroest.structure.Layer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.AcceptAllPasswordAuthenticator;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ShellFactory;

public class BifroestSSHApp {

  InputStream in = null;
  OutputStream out = null;
  Layer rootLayer = null;
  SshServer sshd;
  Example example = new Example();
  String name;
  Banner banner;
  Prompt prompt;

  protected BifroestSSHApp(String name, Layer rootLayer, Banner banner, Prompt prompt) {
    this.rootLayer = rootLayer;
    this.name = name;
    this.banner = banner;
    this.prompt = prompt;
    sshd = SshServer.setUpDefaultServer();
    sshd.setPort(2233);
    SimpleGeneratorHostKeyProvider keyProvider =
        new SimpleGeneratorHostKeyProvider(Paths.get("hostkey"));
    sshd.setKeyPairProvider(keyProvider);
    sshd.setPasswordAuthenticator(AcceptAllPasswordAuthenticator.INSTANCE);
    sshd.setShellFactory(new supplier());
  }

  public void run() {
    try {
      sshd.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public interface NameSetter {
    BifroestSSHApp.RootLayerSetter withName(String name);
  }

  /*public interface CommunicationSetter {
    BifroestSSHApp.RootLayerSetter andCommunication(Communication communication);
  }*/

  public interface RootLayerSetter {
    BifroestSSHApp.OptionalFieldsSetter andEntryPoint(Layer rootLayer);
  }

  public interface OptionalFieldsSetter {
    BifroestSSHApp.OptionalFieldsSetter andBanner(Banner banner);

    BifroestSSHApp.OptionalFieldsSetter andPrompt(Prompt prompt);

    BifroestSSHApp build();
  }

  static class SshAppBuilder
      implements BifroestSSHApp.RootLayerSetter,
          BifroestSSHApp.NameSetter,
          /* BifroestSSHApp.CommunicationSetter,*/
          BifroestSSHApp.OptionalFieldsSetter {

    Layer rootLayer = null;
    String name = null;
    Banner banner = new SimpleContextNameBanner();
    Prompt prompt = Prompt.DEFAULT;
    Communication communication;

    @Override
    public BifroestSSHApp.OptionalFieldsSetter andBanner(Banner banner) {
      this.banner = banner;
      return this;
    }

    @Override
    public BifroestSSHApp.OptionalFieldsSetter andPrompt(Prompt prompt) {
      this.prompt = prompt;
      return this;
    }

    @Override
    public BifroestSSHApp build() {
      return new BifroestSSHApp(name, rootLayer, banner, prompt);
    }

    @Override
    public BifroestSSHApp.OptionalFieldsSetter andEntryPoint(Layer rootLayer) {
      this.rootLayer = rootLayer;
      return this;
    }

    @Override
    public BifroestSSHApp.RootLayerSetter withName(String name) {
      this.name = name;
      return this;
    }

    /*@Override
    public BifroestSSHApp.RootLayerSetter andCommunication(Communication communication) {
      this.communication = communication;
      return this;
    }*/
  }

  public class supplier implements ShellFactory {
    @Override
    public Command createShell(ChannelSession channel) throws IOException {
      return new BifroestCliAppCommand();
    }
  }

  public class BifroestCliAppCommand implements Command, Runnable {
    ExitCallback exit;
    InputStream in;
    OutputStream out;
    Thread thread;
    ChannelSession channelSession;

    @Override
    public void setExitCallback(ExitCallback callback) {
      exit = callback;
    }

    @Override
    public void setErrorStream(OutputStream err) {}

    @Override
    public void setInputStream(InputStream in) {
      this.in = in;
    }

    @Override
    public void setOutputStream(OutputStream out) {
      this.out = out;
    }

    @Override
    public void start(ChannelSession channel, Environment env) throws IOException {

      thread = new Thread(this);
      thread.start();
    }

    @Override
    public void destroy(ChannelSession channel) throws Exception {
      thread.interrupt();
    }

    @Override
    public void run() {
      // String user = channel.getSession().getUsername();
      Communication c = new JLineSSHCommunication(in, out);
      BifroestCliApp app =
          BifroestApp.builder()
              .cli()
              .withName(name)
              .andCommunication(c)
              .andEntryPoint(rootLayer)
              .andBanner(banner)
              .andPrompt(prompt)
              .build();
      app.run();
      exit.onExit(0);
    }
  }
}
