package com.dmoser.codyssey.bifroest.forms.resolvers;

import com.dmoser.codyssey.bifroest.forms.FormElement;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Request;

public class StringResolver implements FormParameterResolver {
  @Override
  public Object retrieveParameter(
      Request request, Communication communication, FormElement formElement) {
    String name = formElement.name();
    if (request.params().containsKey(name)) {
      return request.params().get(name);
    }
    int position = formElement.position();
    if (request.args().size() > position == position >= 0) {
      return request.args().get(position);
    }
    return communication.requestParam(formElement.name(), formElement.msg());
  }
}
