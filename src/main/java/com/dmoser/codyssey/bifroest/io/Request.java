package com.dmoser.codyssey.bifroest.io;

import com.dmoser.codyssey.bifroest.io.communications.RequestOrigin;
import java.util.List;
import java.util.Map;

public record Request(
    RequestOrigin pathOrigin,
    List<String> path,
    String command,
    List<String> args,
    Map<String, String> params) {}
