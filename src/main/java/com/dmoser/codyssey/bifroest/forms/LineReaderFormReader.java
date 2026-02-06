package com.dmoser.codyssey.bifroest.forms;

import com.dmoser.codyssey.bifroest.session.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineReaderFormReader implements FormReader {

  @Override
  public Map<String, String> fillFormElements(List<FormParameter> formParameterList) {
    Map<String, String> formElementMap = new HashMap<>();
    for (FormParameter formParameter : formParameterList) {
      String prompt =
          formParameter.msg() != null
              ? formParameter.msg()
              : "Please enter %s> ".formatted(formParameter.name());
      String value = Session.in().readLine(prompt);
      formElementMap.put(formParameter.name(), value);
    }
    return formElementMap;
  }
}
