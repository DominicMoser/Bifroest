package com.dmoser.codyssey.bifroest.commands;

import com.dmoser.codyssey.bifroest.forms.Form;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Command that handles object creation or update by using a {@link Form} to collect data and a
 * {@link Consumer} to process the resulting object.
 *
 * @param <T> the type of object this command handles
 */
public class InsertCommand<T> implements SimpleCommand {

  protected final Consumer<T> target;
  protected final Supplier<Form<T>> formSupplier;
  private Form<T> form = null;

  /**
   * Constructs an {@code InsertCommand} with a pre-built form.
   *
   * @param target the consumer that will receive the submitted object
   * @param form the form to use for data entry
   */
  public InsertCommand(Consumer<T> target, Form<T> form) {
    this.target = target;
    this.formSupplier = null;
    this.form = form;
  }

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
  public void execute(List<String> params) {
    if (form == null) {
      form = formSupplier.get();
    }
    T object = form.submit();
    target.accept(object);
  }
}
