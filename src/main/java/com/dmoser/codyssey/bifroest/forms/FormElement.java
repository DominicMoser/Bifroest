package com.dmoser.codyssey.bifroest.forms;

import java.lang.reflect.Type;

public record FormElement(Class<?> elementType, Type type, String name, String msg, int position) {}
