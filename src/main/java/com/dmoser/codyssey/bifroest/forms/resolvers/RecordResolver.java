package com.dmoser.codyssey.bifroest.forms.resolvers;

import com.dmoser.codyssey.bifroest.forms.Form;
import com.dmoser.codyssey.bifroest.forms.FormElement;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Request;

public class RecordResolver implements FormParameterResolver {

  @SuppressWarnings("unchecked")
  private <FormType> FormType fillForm(Request request, Form<FormType> form, Communication io) {
    Object[] params = new Object[form.formElements().size()];

    for (int i = 0; i < form.formElements().size(); i++) {
      FormParameterResolver resolver =
          FormParameterResolver.getFormParameterResolverForType(
              form.formElements().get(i).elementType());
      params[i] = resolver.retrieveParameter(request, io, form.formElements().get(i));
    }
    return form.submit(params);
  }

  @Override
  public Object retrieveParameter(
      Request request, Communication communication, FormElement formElement) {
    Class<?> paramType = formElement.elementType();
    return fillForm(request, Form.getForm(paramType), communication);
  }
}
