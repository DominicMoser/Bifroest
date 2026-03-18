package com.dmoser.codyssey.bifroest.forms.flags;

import com.dmoser.codyssey.bifroest.capabilities.Capability;
import com.dmoser.codyssey.bifroest.forms.Form;
import com.dmoser.codyssey.bifroest.forms.FormElement;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class FormFlagHandler implements Capability {

  private final Map<Class<?>, BiFunction<Communication, FormElement, Object>> typeHandlerMap =
      new HashMap<>();

  public FormFlagHandler() {
    typeHandlerMap.put(
        String.class, (io, formElement) -> io.requestParam(formElement.name(), formElement.name()));
  }

  @Override
  public void handleFlag(Flag flag) {
    if (flag instanceof FormFlag<?> formFlag) {
      handleFormFlag(formFlag);
    }
  }

  private <T> T fillForm(Form<T> form, Communication io) {
    Object[] formParameters = fillFormParams(form.formElements(), io);
    try {
      return form.submit(formParameters);
    } catch (Throwable e) {
      throw new RuntimeException();
    }
  }

  private <T> Object[] fillFormParams(List<FormElement> formElementList, Communication io) {
    Object[] params = new Object[formElementList.size()];
    for (int i = 0; i < formElementList.size(); i++) {
      FormElement formElement = formElementList.get(i);
      Class<?> paramType = formElement.elementType();
      if (paramType.isRecord()) {
        params[i] = fillForm(Form.getForm(paramType), io);
        continue;
      }
      params[i] =
          this.typeHandlerMap.getOrDefault(paramType, (a, b) -> null).apply(io, formElement);
    }
    return params;
  }

  private <T> void handleFormFlag(FormFlag<T> formFlag) {

    Form<T> form = Form.getForm(formFlag.objectClass());
    Consumer<T> target = formFlag.target();
    Communication io = Session.get().getIO();

    T dto = fillForm(form, io);
    target.accept(dto);
  }
}
