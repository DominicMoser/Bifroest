package com.dmoser.codyssey.bifroest.forms.resolvers;

import com.dmoser.codyssey.bifroest.forms.FormElement;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Request;

public interface FormParameterResolver {
  FormParameterResolver STRING_RESOLVER = new StringResolver();
  FormParameterResolver INTEGER_RESOLVER = new IntegerResolver();
  FormParameterResolver FLOAT_RESOLVER = new FloatResolver();
  FormParameterResolver BOOLEAN_RESOLVER = new BooleanResolver();

  Object retrieveParameter(Request request, Communication communication, FormElement formElement);
}
