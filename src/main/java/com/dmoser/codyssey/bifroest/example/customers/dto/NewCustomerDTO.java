package com.dmoser.codyssey.bifroest.example.customers.dto;

import com.dmoser.codyssey.bifroest.forms.Form;
import com.dmoser.codyssey.bifroest.forms.FormConstructor;
import java.util.Map;

public record NewCustomerDTO(String firstName, String lastName) {

  @FormConstructor
  public static Form<NewCustomerDTO> getForm() {
    return new Form<NewCustomerDTO>("First Name", "Last Name") {
      @Override
      public NewCustomerDTO submit(Map<String, String> formElements) {
        return new NewCustomerDTO(formElements.get("First Name"), formElements.get("Last Name"));
      }
    };
  }
}
