package com.dmoser.codyssey.bifroest.example.customers;

import com.dmoser.codyssey.bifroest.example.customers.dto.CustomerDTO;
import com.dmoser.codyssey.bifroest.example.customers.dto.NewCustomerDTO;
import com.dmoser.codyssey.bifroest.structure.AbstractLayer;
import com.dmoser.codyssey.bifroest.structure.InsertCommand;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomerLayer extends AbstractLayer {

  CustomerService customerService = new CustomerService();

  public CustomerLayer() {
    super();
    addCommand("add", new InsertCommand<>(customerService::addNew, NewCustomerDTO.class));
    addCommand("update", new InsertCommand<>(customerService::updateCustomer, CustomerDTO.class));
    addCommand(
        "list",
        (p, request) ->
            customerService.list().stream()
                .map(Record::toString)
                .collect(Collectors.joining("\n")));
    addLayer("customer", Pattern.compile("^\\d+$"), new SingleCustomerLayer(customerService));
  }
}
