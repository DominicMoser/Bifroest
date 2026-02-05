package com.dmoser.codyssey.bifroest.forms;

import java.util.List;
import java.util.Map;

public interface FormReader {
  Map<String, String> fillFormElements(List<FormParameter> formParameterList);
}
