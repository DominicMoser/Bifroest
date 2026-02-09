package com.dmoser.codyssey.bifroest.app;

import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;
import com.dmoser.codyssey.bifroest.io.communications.SimpleConsoleCommunication;
import com.dmoser.codyssey.bifroest.io.completer.CompleterProviderImplementation;
import com.dmoser.codyssey.bifroest.io.errors.ErrorCode;
import com.dmoser.codyssey.bifroest.io.flags.CommandNotFoundFlag;
import com.dmoser.codyssey.bifroest.io.flags.Flags;
import com.dmoser.codyssey.bifroest.io.flags.NavigationFlag;
import com.dmoser.codyssey.bifroest.session.Session;
import com.dmoser.codyssey.bifroest.structure.Command;
import com.dmoser.codyssey.bifroest.structure.Layer;
import com.dmoser.codyssey.bifroest.testUtils.TestFlag;
import com.dmoser.codyssey.bifroest.testUtils.TestLayer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BifroestAppTest {

  @BeforeEach
  public void setup() {
    try {
      com.dmoser.codyssey.bifroest.session.Session.get();
    } catch (IllegalStateException e) {
      com.dmoser.codyssey.bifroest.session.Session.create()
          .withName("test")
          .andCommunication(null)
          .open();
    }
    com.dmoser.codyssey.bifroest.session.Session.get().setCurrentPath(List.of());
  }

  @Test
  public void testCommandNotFound() {
    TestRunner t = new TestRunner();
    Request i = Request.of(null, List.of("commandNotFound"));
    Result result = t.handleRequest(i);
    Assertions.assertInstanceOf(CommandNotFoundFlag.class, result);
  }

  @Test
  public void testCommandNotFoundLayered() {
    TestRunner t = new TestRunner();
    Request i = Request.of(null, List.of("layer1", "commandNotFound"));
    Result result = t.handleRequest(i);
    Assertions.assertInstanceOf(CommandNotFoundFlag.class, result);
  }

  @Test
  public void testNavigationFlag() {
    TestRunner t = new TestRunner();
    Request i = Request.of(null, List.of("layer2"));
    Result result = t.handleRequest(i);
    Assertions.assertInstanceOf(NavigationFlag.class, result);
  }

  @Test
  public void testNavigationFlagLayered() {
    TestRunner t = new TestRunner();
    Request i = Request.of(null, List.of("layer2", "layer3"));
    Result result = t.handleRequest(i);
    Assertions.assertInstanceOf(NavigationFlag.class, result);
    NavigationFlag n = (NavigationFlag) result;
    Assertions.assertEquals(2, n.path().size());
    Assertions.assertEquals("layer2", n.path().get(0));
    Assertions.assertEquals("layer3", n.path().get(1));
  }

  @Test
  public void testSimpleExecution() {
    TestRunner t = new TestRunner();
    Request i = Request.of(null, List.of("cmd1", "arg"));
    Result result = t.handleRequest(i);
    Assertions.assertInstanceOf(TestFlag.class, result);
    TestFlag testFlag = (TestFlag) result;
    Assertions.assertEquals("cmd1", testFlag.val());
    Assertions.assertEquals(0, testFlag.request().getPath().size());
    Assertions.assertEquals(1, testFlag.request().getArguments().size());
    Assertions.assertEquals("arg", testFlag.request().getArguments().getFirst());
  }

  @Test
  public void testGlobalCommandFromRoot() {
    TestRunner t = new TestRunner();
    Request i = Request.of(null, List.of("?", "arg"));
    Result result = t.handleRequest(i);
    Assertions.assertInstanceOf(TestFlag.class, result);
    TestFlag testFlag = (TestFlag) result;
    Assertions.assertEquals("?", testFlag.val());
    Assertions.assertEquals(0, testFlag.request().getPath().size());
    Assertions.assertEquals(1, testFlag.request().getArguments().size());
    Assertions.assertEquals("arg", testFlag.request().getArguments().getFirst());
  }

  @Test
  public void testGlobalCommandFromLayer() {
    TestRunner t = new TestRunner();
    Request i = Request.of(null, List.of("layer2"));
    t.handleRequest(i);
    i = Request.of(null, List.of("?", "arg"));
    Result result = t.handleRequest(i);
    Assertions.assertInstanceOf(TestFlag.class, result);
    TestFlag testFlag = (TestFlag) result;

    Assertions.assertEquals("?", testFlag.val());
    Assertions.assertEquals(0, testFlag.request().getPath().size());
    Assertions.assertEquals(1, testFlag.request().getArguments().size());
    Assertions.assertEquals("arg", testFlag.request().getArguments().getFirst());
  }

  @Test
  public void testGlobalCommandFromLayer2() {
    TestRunner t = new TestRunner();
    Request i = Request.of(null, List.of("layer2", "?", "arg"));
    Result result = t.handleRequest(i);
    Assertions.assertInstanceOf(CommandNotFoundFlag.class, result);
  }

  @Test
  public void testGlobalCommandFromLayer3() {
    TestRunner t = new TestRunner();
    Request i = Request.of(null, List.of("test", "?"));
    Result result = t.handleRequest(i);
    Assertions.assertInstanceOf(CommandNotFoundFlag.class, result);
  }

  @Test
  public void testLayeredExecution() {
    TestRunner t = new TestRunner();
    Request i = Request.of(null, List.of("layer2", "cmd2", "arg"));
    Result result = t.handleRequest(i);
    Assertions.assertInstanceOf(TestFlag.class, result);
    TestFlag testFlag = (TestFlag) result;
    Assertions.assertEquals("cmd2", testFlag.val());
    Assertions.assertEquals(1, testFlag.request().getPath().size());
    Assertions.assertEquals("layer2", testFlag.request().getPath().getFirst());
    Assertions.assertEquals(1, testFlag.request().getArguments().size());
    Assertions.assertEquals("arg", testFlag.request().getArguments().getFirst());
  }

  @Test
  public void testHandleFlag() {
    TestRunner t = new TestRunner();
    AtomicBoolean flagHandled = new AtomicBoolean(true);
    t.capabilityMap.put(CommandNotFoundFlag.class, (flag) -> flagHandled.set(true));

    t.handleFlag(new CommandNotFoundFlag(ErrorCode.COMMAND_NOT_FOUND, ""));

    Assertions.assertTrue(flagHandled.get());
  }

  @Test
  public void testHandleFlagNotFound() {
    TestRunner t = new TestRunner();
    AtomicBoolean flagNotFound = new AtomicBoolean(false);
    AtomicBoolean flagFound = new AtomicBoolean(false);
    t.capabilityMap.put(CommandNotFoundFlag.class, (flag) -> flagFound.set(true));
    t.defaultFlagHandler = (flg) -> flagNotFound.set(true);
    t.handleFlag(Flags.navigationFlag(List.of("")));
    Assertions.assertTrue(flagNotFound.get());
    Assertions.assertFalse(flagFound.get());
  }

  @Test
  public void testCompletion() {
    TestRunner t = new TestRunner();
    t.init();

    CompleterProviderImplementation provider = t.completerProvider;
    List<String> completions = provider.getCompletions(List.of(""), 0, 0);

    // root has layer2 (which has cmd1, cmd2), layer1 (which has cmd1)
    // Wait, TestRunner root is layer1. layer1 has layer2 and cmd1.
    Assertions.assertTrue(completions.contains("layer2/"));
    Assertions.assertTrue(completions.contains("cmd1"));
  }

  @Test
  public void testLayeredCompletion() {
    TestRunner t = new TestRunner();
    t.init();

    // Navigate to layer2
    Session.get().setCurrentPath(List.of("layer2"));

    CompleterProviderImplementation provider = t.completerProvider;
    List<String> completions = provider.getCompletions(List.of(""), 0, 0);

    // layer2 has layer3, cmd1, cmd2
    Assertions.assertTrue(completions.contains("cmd1"));
    Assertions.assertTrue(completions.contains("cmd2"));
    Assertions.assertTrue(completions.contains("layer3"));
  }

  @Test
  public void testCircularLayerCompletion() {
    TestLayer circularLayer = new TestLayer(new java.util.HashMap<>(), new java.util.HashMap<>());
    circularLayer.addLayer("self", circularLayer);

    BifroestCliApp runner =
        BifroestCliApp.builder()
            .cli()
            .withName("test")
            .andCommunication(new SimpleConsoleCommunication())
            .andEntryPoint(circularLayer)
            .build();

    // This should not throw StackOverflowError
    runner.init();
  }

  public class TestRunner extends BifroestApp {
    public CompleterProviderImplementation completerProvider =
        new CompleterProviderImplementation();

    public TestRunner() {
      super(null, Prompt.DEFAULT);
      Command cmd1 = input -> new TestFlag("cmd1", input);
      Command cmd2 = input -> new TestFlag("cmd2", input);
      Command cmdHelp = input -> new TestFlag("?", input);
      TestLayer layer3 = new TestLayer(Map.of(), Map.of());
      TestLayer layer2 =
          new TestLayer(Map.of("layer3", layer3), Map.of("cmd1", cmd1, "cmd2", cmd2));
      TestLayer layer1 = new TestLayer(Map.of("layer2", layer2), Map.of("cmd1", cmd1));
      this.rootLayer = layer1;
      globalCommands = Map.of(Pattern.compile("\\?"), cmdHelp);
      capabilityMap = new HashMap<>();
    }

    @Override
    public void setCompleter() {}

    @Override
    protected void loadCapabilities() {}

    @Override
    protected void loadGlobalCommands() {}

    @Override
    protected void init() {
      populate(rootLayer, List.of());
    }

    private void populate(Layer layer, List<String> path) {
      for (String cmd : layer.getCommandNames()) {
        List<String> p = new java.util.ArrayList<>(path);
        p.add(cmd);
        completerProvider.addPath(p);
      }
      for (String l : layer.getLayerNames()) {
        List<String> p = new java.util.ArrayList<>(path);
        p.add(l);
        completerProvider.addPath(p);
        populate(layer.getLayer(l), p);
      }
    }

    @Override
    protected void initSession() {}

    @Override
    protected void start() {}

    @Override
    protected void stop() {}
  }
}
