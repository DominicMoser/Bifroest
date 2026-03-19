package com.dmoser.codyssey.bifroest.forms.resolvers;

import com.dmoser.codyssey.bifroest.forms.FormElement;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FormParameterResolver {
  FormParameterResolver STRING_RESOLVER = new StringResolver();
  FormParameterResolver INTEGER_RESOLVER = new IntegerResolver();
  FormParameterResolver FLOAT_RESOLVER = new FloatResolver();
  FormParameterResolver BOOLEAN_RESOLVER = new BooleanResolver();
  FormParameterResolver RECORD_RESOLVER = new RecordResolver();
  FormParameterResolver LIST_RESOLVER = new ListResolver();

  static FormParameterResolver getFormParameterResolverForType(Class<?> type) {
    Map<Class<?>, FormParameterResolver> typeHandlerMap =
        (Map<Class<?>, FormParameterResolver>) Session.get().getVariable("form_resolvers");
    FormParameterResolver resolver = typeHandlerMap.get(type);
    if (resolver != null) {
      return resolver;
    }
    List<Class<?>> keys = new ArrayList<>();
    for (Class<?> handlerType : typeHandlerMap.keySet()) {
      if (handlerType.isAssignableFrom(type)) {
        return typeHandlerMap.get(handlerType);
      }
    }
    return (request, communication, formElement) -> null;
  }

  Object retrieveParameter(Request request, Communication communication, FormElement formElement);
}
