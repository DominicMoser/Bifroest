package com.dmoser.codyssey.bifroest.example.customers;

import com.dmoser.codyssey.bifroest.structure.AbstractLayer;

public class SingleCustomerLayer extends AbstractLayer {
  CustomerService service;

  public SingleCustomerLayer(CustomerService service) {
    this.service = service;
    super();
    addCommand(
        "get",
        (path, params) -> {
          return service.get(path.getLast());
        });
  }

  @Override
  public boolean isAccessible(String command) {
    return service.has(command);
  }
}
