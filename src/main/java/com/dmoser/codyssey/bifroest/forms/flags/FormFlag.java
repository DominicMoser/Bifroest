package com.dmoser.codyssey.bifroest.forms.flags;

import com.dmoser.codyssey.bifroest.forms.Form;
import com.dmoser.codyssey.bifroest.io.Flag;
import java.util.function.Consumer;

public record FormFlag<T>(Form<T> form, Consumer<T> target) implements Flag {}
