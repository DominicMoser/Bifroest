package com.dmoser.codyssey.bifroest.structure;

import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.flags.NavigationFlag;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NavigationCommandTest {

  Pattern regex = Pattern.compile(new NavigationCommand().getRegex());

  @Test
  public void shouldMatchRoot() {
    Assertions.assertTrue(regex.matcher("/").matches());
  }

  @Test
  public void shouldMatchAbsoluteSingleSegment() {
    Assertions.assertTrue(regex.matcher("/test").matches());
  }

  @Test
  public void shouldMatchAbsoluteSingleSegmentWithTrailingSlash() {
    Assertions.assertTrue(regex.matcher("/test/").matches());
  }

  @Test
  public void shouldMatchRelativeSingleSegmentWithTrailingSlash() {
    Assertions.assertTrue(regex.matcher("test/").matches());
  }

  @Test
  public void shouldMatchRelativeMultiSegment() {
    Assertions.assertTrue(regex.matcher("test/abc").matches());
  }

  @Test
  public void shouldMatchAbsoluteMultiSegmentWithSpaces() {
    Assertions.assertTrue(regex.matcher("/tes t1/test").matches());
  }

  @Test
  public void shouldMatchTwoDots() {
    Assertions.assertTrue(regex.matcher("..").matches());
  }

  @Test
  public void shouldMatchThreeDots() {
    Assertions.assertTrue(regex.matcher("...").matches());
  }

  @Test
  public void shouldMatchManyDots() {
    Assertions.assertTrue(regex.matcher("........").matches());
  }

  @Test
  public void shouldMatchFourDots() {
    Assertions.assertTrue(regex.matcher("....").matches());
  }

  @Test
  public void shouldMatchFiveDots() {
    Assertions.assertTrue(regex.matcher(".....").matches());
  }

  @Test
  public void shouldMatchRelativeSingleDotDot() {
    Assertions.assertTrue(regex.matcher("../test").matches());
  }

  @Test
  public void shouldMatchRelativeDoubleDotDot() {
    Assertions.assertTrue(regex.matcher("../../test").matches());
  }

  @Test
  public void shouldMatchRelativeDoubleDotDotWithMultiSegment() {
    Assertions.assertTrue(regex.matcher("../../test/test").matches());
  }

  @Test
  public void shouldNotMatchSingleWord() {
    Assertions.assertFalse(regex.matcher("test").matches());
  }

  @Test
  public void shouldMatchDotsFollowedBySegment() {
    Assertions.assertTrue(regex.matcher(".../test").matches());
  }

  @Test
  public void shouldMatchManyDotsFollowedBySegment() {
    Assertions.assertTrue(regex.matcher("......../test").matches());
  }

  @Test
  public void shouldNotMatchSegmentFollowedByDots() {
    Assertions.assertFalse(regex.matcher("test/..").matches());
  }

  @Test
  public void shouldNotMatchAbsoluteSegmentFollowedByDots() {
    Assertions.assertFalse(regex.matcher("/test/..").matches());
  }

  @Test
  public void shouldNotMatchSegmentFollowedByDotsWithTrailingSlash() {
    Assertions.assertFalse(regex.matcher("test/../").matches());
  }

  @Test
  public void shouldNotMatchAbsoluteSegmentFollowedByDotsAndSegment() {
    Assertions.assertFalse(regex.matcher("/test/../t").matches());
  }

  @Test
  public void shouldNotMatchComplexInvalidPath() {
    Assertions.assertFalse(regex.matcher("/test/.../.").matches());
  }

  @Test
  public void shouldNotMatchSingleDot() {
    Assertions.assertFalse(regex.matcher(".").matches());
  }

  @Test
  public void shouldNotMatchDoubleSlash() {
    Assertions.assertFalse(regex.matcher("//t").matches());
  }

  @Test
  public void shouldNotMatchDoubleSlashInMiddle() {
    Assertions.assertFalse(regex.matcher("test//t").matches());
  }

  @Test
  public void shouldNotAllowDotDotAfterTextSegment() {
    Assertions.assertFalse(regex.matcher("../test/..").matches());
  }

  @Test
  public void shouldAllowRelativePathWithTextSegments() {
    Assertions.assertTrue(regex.matcher("../test/abc").matches());
  }

  @Test
  public void shouldNotAllowDotDotAfterTextSegmentDeep() {
    Assertions.assertFalse(regex.matcher("../../test/../abc").matches());
  }

  @Test
  public void shouldAllowRelativePathWithTrailingSlash() {
    Assertions.assertTrue(regex.matcher("../../test/abc/").matches());
  }

  @Test
  public void shouldMatchPathWithComplexSegmentsContainingDots() {
    Assertions.assertTrue(regex.matcher("../../...test/.a...bc../").matches());
  }

  @Test
  public void shouldNotMatchSegmentStartingWithDotWithoutSlash() {
    Assertions.assertFalse(regex.matcher(".test").matches());
  }

  @Test
  public void shouldNotMatchSegmentStartingWithDoubleDotWithoutSlash() {
    Assertions.assertFalse(regex.matcher("..test").matches());
  }

  @Test
  public void shouldNotMatchSegmentEndingWithDoubleDot() {
    Assertions.assertFalse(regex.matcher("test..").matches());
  }

  @Test
  public void shouldMatchSegmentStartingWithDoubleDotFollowedBySlash() {
    Assertions.assertTrue(regex.matcher("..test/").matches());
  }

  @Test
  public void shouldMatchAbsoluteSegmentStartingWithDoubleDot() {
    Assertions.assertTrue(regex.matcher("/..test").matches());
  }

  public void testSplitPath(List<String> expected, String testString) {
    List<String> split = new NavigationCommand().splitString(testString);
    Assertions.assertEquals(expected.size(), split.size());
    for (int i = 0; i == expected.size() - 1; i++) {
      Assertions.assertEquals(expected.get(i), split.get(i));
    }
  }

  @Test
  public void shouldSplitThreeDotDots() {
    testSplitPath(List.of("..", "..", ".."), "../../..");
  }

  @Test
  public void shouldSplitThreeDotsIntoTwoDotDots() {
    testSplitPath(List.of("..", ".."), "...");
  }

  @Test
  public void shouldSplitFourDotsIntoThreeDotDots() {
    testSplitPath(List.of("..", "..", ".."), "....");
  }

  @Test
  public void shouldSplitDotsFollowedBySegment() {
    testSplitPath(List.of("..", "..", "test"), ".../test");
  }

  @Test
  public void shouldSplitAbsoluteWithTrailingDotInSegment() {
    testSplitPath(List.of("/", "test.", "test2"), "/test./test2");
  }

  @Test
  public void shouldSplitRelativeWithTrailingDotInSegment() {
    testSplitPath(List.of("..", "test.", "test2"), "../test./test2");
  }

  @Test
  public void shouldSplitRelativeWithLeadingAndTrailingDotInSegment() {
    testSplitPath(List.of(".test.", "test2"), ".test./test2");
  }

  @Test
  public void shouldSplitSimpleAbsolute() {
    testSplitPath(List.of("/", "test1"), "/test1");
  }

  @Test
  public void shouldSplitSpaces() {
    testSplitPath(List.of("/", "test1 "), "/test1 /");
  }

  @Test
  public void shouldSplitSpaces_1() {
    testSplitPath(List.of("/", "test1 "), "/test1 ");
  }

  @Test
  public void shouldSplitSpaces_2() {
    testSplitPath(List.of("/", "test1 ", "test2"), "/test1 /test2");
  }

  @Test
  public void shouldSplitSpaces_3() {
    testSplitPath(List.of("test1 ", "test2"), "test1 /test2");
  }

  private void testExecute(List<String> currentPath, String command, List<String> expectedPath) {
    // Setup session
    try {
      Session.get();
    } catch (IllegalStateException e) {
      Session.create().withName("test").andCommunication(null).open();
    }
    Session.get().setCurrentPath(currentPath);

    NavigationCommand navigationCommand = new NavigationCommand();
    Request request = Request.of(List.of(), command);
    NavigationFlag result = (NavigationFlag) navigationCommand.execute(request);

    Assertions.assertEquals(expectedPath, result.path());
  }

  @Test
  public void shouldExecuteAbsolutePath() {
    testExecute(List.of("a", "b"), "/c/d", List.of("c", "d"));
  }

  @Test
  public void shouldExecuteRelativePath() {
    testExecute(List.of("a", "b"), "c/d", List.of("a", "b", "c", "d"));
  }

  @Test
  public void shouldExecuteRelativePathWithDotDot() {
    testExecute(List.of("a", "b"), "../c", List.of("a", "c"));
  }

  @Test
  public void shouldExecutePathWithSpaces() {
    testExecute(List.of("b"), "\"/c \"", List.of("c "));
  }

  @Test
  public void shouldExecutePathWithSpaces2() {
    testExecute(List.of("b"), "\"test/c /t\"", List.of("b", "test", "c ", "t"));
  }

  @Test
  public void shouldExecuteRelativePathWithManyDotDot() {
    testExecute(List.of("a", "b"), "../../c", List.of("c"));
  }

  @Test
  public void shouldExecuteRelativePathWithTooManyDotDot() {
    testExecute(List.of("a", "b"), "../../../c", List.of("c"));
  }

  @Test
  public void shouldExecuteThreeDots() {
    testExecute(List.of("a", "b", "c"), "...", List.of("a"));
  }

  @Test
  public void shouldExecuteAbsolutePathWithDotDot() {
    // Though regex might prevent /.. in some cases, execute should handle it if passed
    testExecute(List.of("a", "b"), "/c/../d", List.of("d"));
  }

  @Test
  public void shouldExecuteRoot() {
    testExecute(List.of("a", "b"), "/", List.of());
  }

  private void setupSession() {
    try {
      Session.get();
    } catch (IllegalStateException e) {
      Session.create().withName("test").andCommunication(null).open();
    }
    Session.get().setCurrentPath(List.of());
  }
}
