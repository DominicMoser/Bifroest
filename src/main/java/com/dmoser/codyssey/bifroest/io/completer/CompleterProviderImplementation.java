package com.dmoser.codyssey.bifroest.io.completer;

import com.dmoser.codyssey.bifroest.session.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleterProviderImplementation implements CompleterProvider {

  private final Node root = new Node();

  public void addPath(List<String> path) {
    Node current = root;
    for (String segment : path) {
      current = current.children.computeIfAbsent(segment, k -> new Node());
    }
  }

  @Override
  public List<String> getCompletions(List<String> words, int wordIndex, int wordCursor) {
    Node context = root;
    for (String segment : Session.get().getCurrentPath()) {
      Node next = context.children.get(segment);
      if (next == null) break;
      context = next;
    }

    if (words.isEmpty() || wordIndex >= words.size()) {
      return getCandidates(context);
    }

    String wordToComplete = words.get(wordIndex);
    return findCompletions(context, wordToComplete);
  }

  private List<String> findCompletions(Node node, String input) {
    int lastSlash = input.lastIndexOf('/');
    if (lastSlash == -1) {
      // Complete in current node
      return matchCandidates(node, input, "");
    } else {
      String prefix = input.substring(0, lastSlash);
      String currentInput = input.substring(lastSlash + 1);
      String[] path = prefix.split("/");

      Node target = node;
      if (prefix.startsWith("/")) {
        // Handle absolute path if root is passed?
        // For now, let's assume relative to current node or handle root specifically if needed
      }

      StringBuilder resolvedPrefix = new StringBuilder();
      for (String segment : path) {
        if (segment.isEmpty()) {
          if (resolvedPrefix.length() == 0) resolvedPrefix.append("/");
          continue;
        }
        target = target.children.get(segment);
        if (target == null) return List.of();
        resolvedPrefix.append(segment).append("/");
      }
      if (prefix.endsWith("/") && !prefix.equals("/")) {
        // already handled by split mostly
      }

      return matchCandidates(target, currentInput, prefix.endsWith("/") ? prefix : prefix + "/");
    }
  }

  private List<String> matchCandidates(Node node, String input, String prefix) {
    List<String> matches = new ArrayList<>();
    for (Map.Entry<String, Node> entry : node.children.entrySet()) {
      if (entry.getKey().startsWith(input)) {
        String completion = prefix + entry.getKey();
        if (!entry.getValue().children.isEmpty()) {
          completion += "/";
        }
        matches.add(completion);
      }
    }
    return matches;
  }

  private List<String> getCandidates(Node node) {
    List<String> candidates = new ArrayList<>();
    for (Map.Entry<String, Node> entry : node.children.entrySet()) {
      String candidate = entry.getKey();
      if (!entry.getValue().children.isEmpty()) {
        candidate += "/";
      }
      candidates.add(candidate);
    }
    return candidates;
  }

  private static class Node {
    Map<String, Node> children = new HashMap<>();
  }
}
