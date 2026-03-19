package com.dmoser.codyssey.bifroest.forms.flags;

import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.io.Request;
import java.util.function.Consumer;

public record FormFlag<T>(Class<T> objectClass, Consumer<T> target, Request request)
    implements Flag {}
