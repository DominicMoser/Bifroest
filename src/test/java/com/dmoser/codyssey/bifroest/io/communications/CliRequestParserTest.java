package com.dmoser.codyssey.bifroest.io.communications;

import com.dmoser.codyssey.bifroest.io.Request;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CliRequestParserTest {

  @Test
  public void testRegex() {
    String input = "/full/path/command param1 param2 -arg test";
    CliRequestParser tester = new CliRequestParser();
    var result = tester.splitInput(input);
    Assertions.assertEquals(4, result.length);
    Assertions.assertEquals("/", result[0]);
    Assertions.assertEquals("full/path/", result[1]);
    Assertions.assertEquals("command", result[2]);
    Assertions.assertEquals("param1 param2 -arg test", result[3]);
  }

  @Test
  public void testRegex1() {
    String input = "/full/path/command";
    CliRequestParser tester = new CliRequestParser();
    var result = tester.splitInput(input);
    Assertions.assertEquals(4, result.length);
    Assertions.assertEquals("/", result[0]);
    Assertions.assertEquals("full/path/", result[1]);
    Assertions.assertEquals("command", result[2]);
    Assertions.assertEquals("", result[3]);
  }

  @Test
  public void testRegex4() {
    String input = "full/path/command";
    CliRequestParser tester = new CliRequestParser();
    var result = tester.splitInput(input);
    Assertions.assertEquals(4, result.length);
    Assertions.assertEquals("", result[0]);
    Assertions.assertEquals("full/path/", result[1]);
    Assertions.assertEquals("command", result[2]);
    Assertions.assertEquals("", result[3]);
  }

  @Test
  public void testRegex6() {
    String input = "/test/";
    CliRequestParser tester = new CliRequestParser();
    var result = tester.splitInput(input);
    Assertions.assertEquals(4, result.length);
    Assertions.assertEquals("/", result[0]);
    Assertions.assertEquals("", result[1]);
    Assertions.assertEquals("test/", result[2]);
    Assertions.assertEquals("", result[3]);
  }

  @Test
  public void testRegex2() {
    String input = "/full";
    CliRequestParser tester = new CliRequestParser();
    var result = tester.splitInput(input);
    Assertions.assertEquals(4, result.length);
    Assertions.assertEquals("/", result[0]);
    Assertions.assertEquals("", result[1]);
    Assertions.assertEquals("full", result[2]);
    Assertions.assertEquals("", result[3]);
  }

  @Test
  public void testRegex7() {
    String input = "/";
    CliRequestParser tester = new CliRequestParser();
    var result = tester.splitInput(input);
    Assertions.assertEquals(4, result.length);
    Assertions.assertEquals("", result[0]);
    Assertions.assertEquals("", result[1]);
    Assertions.assertEquals("/", result[2]);
    Assertions.assertEquals("", result[3]);
  }

  @Test
  public void testRegex3() {
    String input = "/path/to/ command";
    CliRequestParser tester = new CliRequestParser();
    var result = tester.splitInput(input);
    Assertions.assertEquals(4, result.length);
    Assertions.assertEquals("/", result[0]);
    Assertions.assertEquals("path/to/", result[1]);
    Assertions.assertEquals("command", result[2]);
    Assertions.assertEquals("", result[3]);
  }

  @Test
  public void testRegex8() {
    String input = "../../../";
    CliRequestParser tester = new CliRequestParser();
    var result = tester.splitInput(input);
    Assertions.assertEquals(4, result.length);
    Assertions.assertEquals("", result[0]);
    Assertions.assertEquals("", result[1]);
    Assertions.assertEquals("../../../", result[2]);
    Assertions.assertEquals("", result[3]);
  }

  @Test
  public void testRegexRelative() {
    String input = "./command";
    CliRequestParser tester = new CliRequestParser();
    var result = tester.splitInput(input);
    Assertions.assertEquals(4, result.length);
    Assertions.assertEquals("./", result[0]);
    Assertions.assertEquals("", result[1]);
    Assertions.assertEquals("command", result[2]);
    Assertions.assertEquals("", result[3]);
  }

  @Test
  public void testRegex9() {
    String input = "full/path/command/";
    CliRequestParser tester = new CliRequestParser();
    var result = tester.splitInput(input);
    Assertions.assertEquals(4, result.length);
    Assertions.assertEquals("", result[0]);
    Assertions.assertEquals("full/path/", result[1]);
    Assertions.assertEquals("command/", result[2]);
    Assertions.assertEquals("", result[3]);
  }

  @Test
  public void testParseArgs() {
    String argString = "test1 test2 -testKey testValue test3";
    List<String> args = new ArrayList<>();
    Map<String, String> params = new HashMap<>();
    CliRequestParser tester = new CliRequestParser();
    tester.parseArgs(argString, args, params);
    Assertions.assertEquals(3, args.size());
    Assertions.assertEquals(1, params.size());
    Assertions.assertTrue(params.containsKey("testKey"));
    Assertions.assertEquals("testValue", params.get("testKey"));
    Assertions.assertEquals("test1", args.get(0));
    Assertions.assertEquals("test2", args.get(1));
    Assertions.assertEquals("test3", args.get(2));
  }

  @Test
  public void testParseInput() {
    String argString = "/full/path/ command test1 test2 -testKey testValue test3";
    CliRequestParser tester = new CliRequestParser();
    Request request = tester.parseInput(argString);
    Assertions.assertEquals(RequestOrigin.ROOT, request.pathOrigin());
    Assertions.assertEquals(2, request.path().size());
    Assertions.assertEquals("full", request.path().get(0));
    Assertions.assertEquals("path", request.path().get(1));
    Assertions.assertEquals("command", request.command());
    Assertions.assertEquals(3, request.args().size());
    Assertions.assertEquals("test1", request.args().get(0));
    Assertions.assertEquals("test2", request.args().get(1));
    Assertions.assertEquals("test3", request.args().get(2));
    Assertions.assertTrue(request.params().containsKey("testKey"));
    Assertions.assertEquals("testValue", request.params().get("testKey"));
  }

  @Test
  public void testParseInputRelative() {
    String argString = "./command test1";
    CliRequestParser tester = new CliRequestParser();
    Request request = tester.parseInput(argString);
    Assertions.assertEquals(RequestOrigin.RELATIVE, request.pathOrigin());
    Assertions.assertEquals(0, request.path().size());
    Assertions.assertEquals("command", request.command());
    Assertions.assertEquals(1, request.args().size());
    Assertions.assertEquals("test1", request.args().get(0));
  }
}
