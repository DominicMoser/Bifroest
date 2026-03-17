package com.dmoser.codyssey.bifroest.example.customers;

import com.dmoser.codyssey.bifroest.session.Session;
import com.dmoser.codyssey.bifroest.structure.AbstractLayer;

public class SingleCustomerLayer extends AbstractLayer {
  CustomerService service;

  public SingleCustomerLayer(CustomerService service) {
    this.service = service;
    super();
    addCommand(
        "get",
        (_) -> service.get((String) Session.get().getVariable(this.getLayerUUID() + "invoke")));
  }

  @Override
  public boolean isAccessible(String callName) {
    return service.has(callName);
  }
}
