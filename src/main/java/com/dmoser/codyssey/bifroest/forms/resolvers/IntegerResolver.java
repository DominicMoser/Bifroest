package com.dmoser.codyssey.bifroest.forms.resolvers;

import com.dmoser.codyssey.bifroest.forms.FormElement;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Request;

public class IntegerResolver extends StringResolver {
  @Override
  public Object retrieveParameter(
      Request request, Communication communication, FormElement formElement) {
    String result = (String) super.retrieveParameter(request, communication, formElement);
    return Integer.parseInt(result);
  }
}
