package com.dmoser.codyssey.bifroest.forms;

import com.dmoser.codyssey.bifroest.forms.annotations.FormMsg;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a data entry mechanism for a specific type {@code T}.
 *
 * @param <FormType> the type of object produced by this form
 */
public record Form<FormType>(List<FormElement> formElements, MethodHandle elementConstructor) {

  private static final Map<Class<?>, Form<?>> cachedForms = new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  private static <FormType> Optional<Form<FormType>> getCachedForm(Class<FormType> dtoClass) {
    Form<?> form = cachedForms.get(dtoClass);
    return Optional.ofNullable((Form<FormType>) cachedForms.get(dtoClass));
  }

  private static void cacheForm(Class<?> dtoClass, Form<?> form) {
    cachedForms.put(dtoClass, form);
  }

  /**
   * Attempts to find and instantiate a form for the given class. It searches for a method annotated
   *
   * @param dtoClass the class to find a form for
   * @param <FormType> the type of the object the form handles
   * @return a {@link Form} instance if found, otherwise {@code null}
   */
  public static <FormType> Form<FormType> getForm(Class<FormType> dtoClass) {
    Optional<Form<FormType>> form = getCachedForm(dtoClass);
    if (form.isPresent()) {
      return form.get();
    }

    form = getRecordForm(dtoClass);
    if (form.isPresent()) {
      cacheForm(dtoClass, form.get());
      return form.get();
    }
    throw new RuntimeException("Form could not be found");
  }

  /**
   * Creates a Form a Record
   *
   * @param dtoClass
   * @return
   * @param <FormType>
   */
  private static <FormType> Optional<Form<FormType>> getRecordForm(Class<FormType> dtoClass) {
    try {
      if (!dtoClass.isRecord()) {
        return Optional.empty();
      }
      RecordComponent[] components = dtoClass.getRecordComponents();
      Class<?>[] paramTypes = new Class<?>[components.length];
      List<FormElement> formElementList = new ArrayList<>();

      for (int i = 0; i < components.length; i++) {
        Class<?> paramType = components[i].getType();
        String paramFieldName = components[i].getName();
        FormMsg paramMsgAnnotation = components[i].getAnnotation(FormMsg.class);
        String paramMsg = paramMsgAnnotation != null ? paramMsgAnnotation.value() : paramFieldName;
        formElementList.add(new FormElement(paramType, paramFieldName, paramMsg, i));
        paramTypes[i] = paramType;
      }
      MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
      MethodType methodType = MethodType.methodType(void.class, paramTypes);
      MethodHandle recordConstructor = publicLookup.findConstructor(dtoClass, methodType);
      MethodHandle constructorSpreader =
          recordConstructor.asSpreader(Object[].class, components.length);
      Form<FormType> form = new Form<FormType>(formElementList, constructorSpreader);
      return Optional.of(form);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Submits the form, collecting user input and returning an instance of type {@code T}.
   *
   * @return the created or updated object
   */
  @SuppressWarnings("unchecked")
  public FormType submit(Object[] formParameters) {
    try {
      return (FormType) elementConstructor().invoke(formParameters);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
