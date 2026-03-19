package com.dmoser.codyssey.bifroest.forms.resolvers;

import com.dmoser.codyssey.bifroest.forms.FormElement;
import com.dmoser.codyssey.bifroest.io.Communication;
import com.dmoser.codyssey.bifroest.io.Request;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListResolver implements FormParameterResolver {

  @SuppressWarnings("unchecked")
  private static <T> List<T> castList(List<?> rawList, Class<T> elementType) {
    List<T> result = new ArrayList<>(rawList.size());

    for (Object o : rawList) {
      result.add(elementType.cast(o));
    }

    return result;
  }

  @Override
  public Object retrieveParameter(
      Request request, Communication communication, FormElement formElement) {
    Type type = formElement.type();
    List returnList = new ArrayList();
    if (type instanceof ParameterizedType pt) {
      Type[] typeArgs = pt.getActualTypeArguments();

      if (typeArgs.length == 1 && typeArgs[0] instanceof Class<?> listElementType) {
        FormParameterResolver resolver =
            FormParameterResolver.getFormParameterResolverForType(listElementType);
        int size = Integer.parseInt(communication.requestParam(formElement.name() + "size", ""));
        for (int i = 0; i < size; i++) {
          returnList.add(
              resolver.retrieveParameter(
                  request,
                  communication,
                  new FormElement(
                      listElementType,
                      listElementType,
                      formElement.name(),
                      formElement.msg(),
                      -1)));
        }
        return castList(returnList, listElementType);
      }
    }
    return null;
  }
}
