package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.forms.Form;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;
import com.dmoser.codyssey.bifroest.session.Session;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Command that handles object creation or update by using a {@link Form} to collect data and a
 * {@link Consumer} to process the resulting object.
 *
 * @param <T> the type of object this command handles
 */
public class InsertCommand<T> implements Command {

  protected final Consumer<T> target;
  protected final Supplier<Form<T>> formSupplier;
  protected Form<T> form;

  /**
   * Constructs an {@code InsertCommand} with a supplier for lazy form initialization.
   *
   * @param target the consumer that will receive the submitted object
   * @param formSupplier a supplier that provides the form instance
   */
  public InsertCommand(Consumer<T> target, Supplier<Form<T>> formSupplier) {
    this.target = target;
    this.formSupplier = formSupplier;
  }

  /**
   * Constructs an {@code InsertCommand} that will try to automatically find the form for the given
   * class using {@link Form#getForm(Class)}.
   *
   * @param target the consumer that will receive the submitted object
   * @param objectClass the class of the object to be created/updated
   */
  public InsertCommand(Consumer<T> target, Class<T> objectClass) {
    this.target = target;
    this.formSupplier = () -> Form.getForm(objectClass);
  }

  @Override
  public Result execute(Request request) {
    if (form == null) {
      form = formSupplier.get();
    }
    try {
      @SuppressWarnings("unchecked")
      T object = (T) Session.submitForm(form);
      target.accept(object);
      return null;
    } catch (Exception e) {
      throw new AssertionError();
    }
  }
}
