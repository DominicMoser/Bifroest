package com.dmoser.codyssey.bifroest.forms.flags;

import com.dmoser.codyssey.bifroest.capabilities.Capability;
import com.dmoser.codyssey.bifroest.forms.Form;
import com.dmoser.codyssey.bifroest.forms.FormElement;
import com.dmoser.codyssey.bifroest.forms.resolvers.FormParameterResolver;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FormFlagHandler implements Capability {

  protected final Map<Class<?>, FormParameterResolver> typeHandlerMap = new HashMap<>();

  public FormFlagHandler() {
    registerTypes();
  }

  protected void registerTypes() {
    registerType(String.class, FormParameterResolver.STRING_RESOLVER);
    registerType(Integer.class, FormParameterResolver.INTEGER_RESOLVER);
    registerType(Float.class, FormParameterResolver.FLOAT_RESOLVER);
    registerType(Boolean.class, FormParameterResolver.BOOLEAN_RESOLVER);
  }

  public void registerType(Class<?> type, FormParameterResolver parameterResolver) {
    typeHandlerMap.put(type, parameterResolver);
  }

  @Override
  public void handleFlag(Flag flag) {
    if (flag instanceof FormFlag<?> formFlag) {
      handleFormFlag(formFlag);
    }
  }

  private <T> T fillForm(Request request, Form<T> form, Communication io) {
    Object[] formParameters = fillFormParams(request, form.formElements(), io);
    try {
      return form.submit(formParameters);
    } catch (Throwable e) {
      throw new RuntimeException();
    }
  }

  private <T> Object[] fillFormParams(
      Request request, List<FormElement> formElementList, Communication io) {
    Object[] params = new Object[formElementList.size()];
    for (int i = 0; i < formElementList.size(); i++) {
      FormElement formElement = formElementList.get(i);
      Class<?> paramType = formElement.elementType();
      if (paramType.isRecord()) {
        params[i] = fillForm(request, Form.getForm(paramType), io);
        continue;
      }
      params[i] =
          this.typeHandlerMap
              .getOrDefault(paramType, (a, b, c) -> null)
              .retrieveParameter(request, io, formElement);
    }
    return params;
  }

  private <T> void handleFormFlag(FormFlag<T> formFlag) {
    Request request = formFlag.request();
    Form<T> form = Form.getForm(formFlag.objectClass());
    Consumer<T> target = formFlag.target();
    Communication io = Session.get().getIO();

    T dto = fillForm(request, form, io);
    target.accept(dto);
  }
}
