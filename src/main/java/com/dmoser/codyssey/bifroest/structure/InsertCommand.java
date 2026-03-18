package com.dmoser.codyssey.bifroest.structure;

import com.dmoser.codyssey.bifroest.forms.Form;
import com.dmoser.codyssey.bifroest.forms.flags.FormFlag;
import com.dmoser.codyssey.bifroest.io.Request;
import com.dmoser.codyssey.bifroest.io.Result;
import java.util.function.Consumer;

/**
 * Command that handles object creation or update by using a {@link Form} to collect data and a
 * {@link Consumer} to process the resulting object.
 *
 * @param <T> the type of object this command handles
 */
public class InsertCommand<T> implements Command {

  protected final Consumer<T> target;
  protected final Class<T> objectClass;

  /**
   * Constructs an {@code InsertCommand} that will try to automatically find the form for the given
   * class using {@link Form#getForm(Class)}.
   *
   * @param target the consumer that will receive the submitted object
   * @param objectClass the class of the object to be created/updated
   */
  public InsertCommand(Consumer<T> target, Class<T> objectClass) {
    this.target = target;
    this.objectClass = objectClass;
  }

  @Override
  public Result execute(Request request) {
    return new FormFlag<T>(objectClass, target);
  }
}
