package com.dmoser.codyssey.bifroest.runners;

import com.dmoser.codyssey.bifroest.commands.Command;
import com.dmoser.codyssey.bifroest.flags.CommandNotFoundFlag;
import com.dmoser.codyssey.bifroest.flags.Flags;
import com.dmoser.codyssey.bifroest.flags.NavigationFlag;
import com.dmoser.codyssey.bifroest.io.Prompt;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;
import com.dmoser.codyssey.bifroest.testUtils.TestFlag;
import com.dmoser.codyssey.bifroest.testUtils.TestLayer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractRunnerTest {

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
    t.flagHandlers.put(CommandNotFoundFlag.class, (flag) -> flagHandled.set(true));

    t.handleFlag(Flags.COMMAND_NOT_FOUND);

    Assertions.assertTrue(flagHandled.get());
  }

  @Test
  public void testHandleFlagNotFound() {
    TestRunner t = new TestRunner();
    AtomicBoolean flagNotFound = new AtomicBoolean(false);
    AtomicBoolean flagFound = new AtomicBoolean(false);
    t.flagHandlers.put(CommandNotFoundFlag.class, (flag) -> flagFound.set(true));
    t.defaultFlagHandler = (flg) -> flagNotFound.set(true);
    t.handleFlag(Flags.navigationFlag(List.of("")));
    Assertions.assertTrue(flagNotFound.get());
    Assertions.assertFalse(flagFound.get());
  }

  public class TestRunner extends AbstractRunner {
    static Command cmd1 = input -> new TestFlag("cmd1", input);
    static Command cmd2 = input -> new TestFlag("cmd2", input);
    static Command cmdHelp = input -> new TestFlag("?", input);
    static TestLayer layer3 = new TestLayer(Map.of(), Map.of());
    static TestLayer layer2 =
        new TestLayer(Map.of("layer3", layer3), Map.of("cmd1", cmd1, "cmd2", cmd2));
    static TestLayer layer1 = new TestLayer(Map.of("layer2", layer2), Map.of("cmd1", cmd1));

    public TestRunner() {
      super(layer1, Prompt.DEFAULT);
      globalCommands = Map.of(Pattern.compile("\\?"), cmdHelp);
      flagHandlers = new HashMap<>();
    }

    @Override
    protected void loadGlobalCommands() {}

    @Override
    protected void init() {}

    @Override
    protected void initSession() {}

    @Override
    protected void start() {}

    @Override
    protected void stop() {}
  }
}
