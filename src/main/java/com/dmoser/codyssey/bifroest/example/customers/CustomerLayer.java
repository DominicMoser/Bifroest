package com.dmoser.codyssey.bifroest.example.customers;

import com.dmoser.codyssey.bifroest.commands.InsertCommand;
import com.dmoser.codyssey.bifroest.example.customers.dto.CustomerDTO;
import com.dmoser.codyssey.bifroest.example.customers.dto.NewCustomerDTO;
import com.dmoser.codyssey.bifroest.layers.Layer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerLayer extends Layer {

  CustomerService customerService = new CustomerService();

  public CustomerLayer() {
    super("customers");
    addCommand("add", new InsertCommand<>(customerService::addNew, NewCustomerDTO.class));
    addCommand("update", new InsertCommand<>(customerService::updateCustomer, CustomerDTO.class));
    addCommand(
        "list",
        params ->
            customerService.list().stream()
                .map(s -> s.toString())
                .collect(Collectors.joining("\n")));
  }

  private List<String> getPath() {
    return Arrays.stream(getLocation().split("/")).filter(s -> !s.isEmpty()).toList();
  }
}
