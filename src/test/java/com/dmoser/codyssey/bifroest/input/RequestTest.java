package com.dmoser.codyssey.bifroest.input;

import com.dmoser.codyssey.bifroest.io.Request;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequestTest {

  @Test
  public void testEmptyInputNotAllowed() {
    List<String> test = new ArrayList<>();
    Assertions.assertThrows(IllegalStateException.class, () -> Request.of(null, test));
  }

  @Test
  public void testNullInputNotAllowed() {
    List<String> inputList = null;
    Assertions.assertThrows(IllegalStateException.class, () -> Request.of(null, inputList));
  }

  @Test
  public void testNullStringInputNotAllowed() {
    String inputString = null;
    Assertions.assertThrows(IllegalStateException.class, () -> Request.of(null, inputString));
  }

  @Test
  public void testNullOrigin() {
    List<String> test = new ArrayList<>();
    test.add("command");

    Request i = Request.of(null, test);
    Assertions.assertEquals(0, i.getOrigin().size());
  }

  @Test
  public void testOrigin() {
    List<String> test = new ArrayList<>();
    test.add("command");
    Request i = Request.of(List.of("abc", "def"), test);
    Assertions.assertEquals(2, i.getOrigin().size());
  }

  @Test
  public void testUnmutable() {
    List<String> test = new ArrayList<>();
    test.add("command");
    test.add("argument");

    Request request = Request.of(null, test);

    test.removeFirst();

    Assertions.assertEquals(1, request.getArguments().size());
    Assertions.assertEquals("argument", request.getArguments().getFirst());
    Assertions.assertEquals("command", request.getCommand());
  }

  @Test
  public void testMovePointerSingleElement() {
    List<String> test = new ArrayList<>();
    test.add("command");
    Request request = Request.of(null, test);

    Assertions.assertEquals("command", request.getCommand());
    Assertions.assertFalse(request.movePointer());
    Assertions.assertEquals("command", request.getCommand());
    Assertions.assertEquals(0, request.getArguments().size());
  }

  @Test
  public void testMovePointerMultipleElements() {
    List<String> test = new ArrayList<>();
    test.add("path1");
    test.add("path2");
    test.add("command");
    test.add("argument1");
    test.add("argument2");
    Request request = Request.of(null, test);

    Assertions.assertEquals(0, request.getPath().size());
    Assertions.assertEquals("path1", request.getCommand());
    Assertions.assertEquals(4, request.getArguments().size());

    Assertions.assertTrue(request.movePointer());
    Assertions.assertTrue(request.movePointer());

    Assertions.assertEquals(2, request.getPath().size());
    Assertions.assertEquals("command", request.getCommand());
    Assertions.assertEquals(2, request.getArguments().size());
  }

  @Test
  public void testResetPointer() {
    List<String> test = new ArrayList<>();
    test.add("command");
    test.add("argument");

    Request request = Request.of(null, test);

    Assertions.assertTrue(request.movePointer());
    Assertions.assertEquals("argument", request.getCommand());
    request.resetPointer();
    Assertions.assertEquals("command", request.getCommand());
  }

  @Test
  public void testToString() {
    List<String> test = List.of("path1", "path2", "command", "arg1", "arg2");
    Request request = Request.of(null, test);

    // pointer at 0
    Assertions.assertEquals("path1 [path2, command, arg1, arg2]", request.toString());

    // pointer at 2 (command)
    request.movePointer();
    request.movePointer();
    Assertions.assertEquals("(path1/path2) command [arg1, arg2]", request.toString());
  }

  @Test
  public void testParseInputString() {
    String test = "abc def \" das ist ein test \"  g";
    List<String> result = Request.parseInputString(test);
    Assertions.assertEquals(4, result.size());
    Assertions.assertEquals("abc", result.get(0));
    Assertions.assertEquals("def", result.get(1));
    Assertions.assertEquals(" das ist ein test ", result.get(2));
    Assertions.assertEquals("g", result.get(3));
  }
}
