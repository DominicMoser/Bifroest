package com.dmoser.codyssey.bifroest.example.customers.dto;

import com.dmoser.codyssey.bifroest.forms.Form;
import com.dmoser.codyssey.bifroest.forms.FormConstructor;
import java.util.Map;

public record CustomerDTO(String uuid, String firstName, String lastName) {
  @FormConstructor
  public static Form<CustomerDTO> getForm() {
    return new Form<CustomerDTO>("uuid", "First Name", "Last Name") {
      @Override
      public CustomerDTO submit(Map<String, String> formElements) {
        return new CustomerDTO(
            formElements.get("uuid"),
            formElements.get("First Name"),
            formElements.get("Last Name"));
      }
    };
  }
}
