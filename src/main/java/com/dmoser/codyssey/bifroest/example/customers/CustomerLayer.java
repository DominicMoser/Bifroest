package com.dmoser.codyssey.bifroest.example.customers;

import com.dmoser.codyssey.bifroest.commands.InsertCommand;
import com.dmoser.codyssey.bifroest.example.customers.dto.CustomerDTO;
import com.dmoser.codyssey.bifroest.example.customers.dto.NewCustomerDTO;
import com.dmoser.codyssey.bifroest.io.Response;
import com.dmoser.codyssey.bifroest.layers.Layer;
import java.util.stream.Collectors;

public class CustomerLayer extends Layer {

  CustomerService customerService = new CustomerService();

  public CustomerLayer() {
    super();
    addCommand("add", new InsertCommand<>(customerService::addNew, NewCustomerDTO.class));
    addCommand("update", new InsertCommand<>(customerService::updateCustomer, CustomerDTO.class));
    addCommand(
        "list",
        _ ->
            (Response)
                () ->
                    customerService.list().stream()
                        .map(Record::toString)
                        .collect(Collectors.joining("\n")));
  }
}
