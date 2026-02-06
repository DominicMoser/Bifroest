package com.dmoser.codyssey.bifroest.forms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Represents a data entry mechanism for a specific type {@code T}.
 *
 * @param <T> the type of object produced by this form
 */
public abstract class Form<T> {

  List<FormParameter> formParameterList = new ArrayList<>();

  public Form(FormParameter first, FormParameter... formParameters) {
    formParameterList.add(first);
    formParameterList.addAll(Arrays.asList(formParameters));
  }

  public Form(String firstFormParameter, String... formParameters) {
    formParameterList.add(new FormParameter(firstFormParameter));
    formParameterList.addAll(Arrays.stream(formParameters).map(FormParameter::new).toList());
  }

  /**
   * Attempts to find and instantiate a form for the given class. It searches for a method annotated
   * with {@link FormConstructor} in the class.
   *
   * @param dtoClass the class to find a form for
   * @param <FormType> the type of the object the form handles
   * @return a {@link Form} instance if found, otherwise {@code null}
   */
  public static <FormType> Form<FormType> getForm(Class<FormType> dtoClass) {
    Optional<Form<FormType>> formOptional = getByAnnotation(dtoClass);
    return formOptional.orElse(null);
  }

  /**
   * Uses reflection to find a static factory method annotated with {@link FormConstructor} and
   * invokes it to obtain a {@link Form} instance.
   *
   * @param dtoClass the class to search for the annotation
   * @param <FormType> the type of the object the form handles
   * @return an {@link Optional} containing the {@link Form} if found and successfully invoked
   */
  static <FormType> Optional<Form<FormType>> getByAnnotation(Class<FormType> dtoClass) {
    Optional<Method> methodOptional =
        Arrays.stream(dtoClass.getMethods())
            .filter(m -> m.isAnnotationPresent(FormConstructor.class))
            .findAny();
    if (methodOptional.isEmpty()) {
      return Optional.empty();
    }
    Method method = methodOptional.get();

    if (!Modifier.isStatic(method.getModifiers())) {
      return Optional.empty();
    }
    if (!Form.class.isAssignableFrom(method.getReturnType())) {
      return Optional.empty();
    }

    try {
      @SuppressWarnings("unchecked")
      Form<FormType> form = (Form<FormType>) method.invoke(null);
      return Optional.of(form);
    } catch (InvocationTargetException | IllegalAccessException e) {
      return Optional.empty();
    } catch (ClassCastException e) {
      throw new IllegalStateException("Factory method did not return the right type");
    }
  }

  /**
   * Submits the form, collecting user input and returning an instance of type {@code T}.
   *
   * @return the created or updated object
   */
  public abstract T submit(Map<String, String> formElements);

  public List<FormParameter> getFormParameters() {
    return new ArrayList<>(formParameterList);
  }
}
