package com.dmoser.codyssey.bifroest.forms.flags;

import com.dmoser.codyssey.bifroest.capabilities.Capability;
import com.dmoser.codyssey.bifroest.forms.Form;
import com.dmoser.codyssey.bifroest.forms.FormParameter;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Flag;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FormFlagHandler implements Capability {
  @Override
  public void handleFlag(Flag flag) {
    if (flag instanceof FormFlag<?> formFlag) {
      handleFormFlag(formFlag);
    }
  }

  private <T> void handleFormFlag(FormFlag<T> formFlag) {
    Form<T> form = formFlag.form();
    Consumer<T> target = formFlag.target();
    Communication io = Session.get().getIO();
    Map<String, String> formElementMap = new HashMap<>();
    for (FormParameter formParameter : form.getFormParameters()) {
      String formParamMsg = formParameter.msg();
      String formParamName = formParameter.name();
      String value = io.getParam(formParamName, formParamMsg);
      formElementMap.put(formParameter.name(), value);
    }
    target.accept(form.submit(formElementMap));
  }
}
