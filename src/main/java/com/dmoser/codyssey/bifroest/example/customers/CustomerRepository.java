package com.dmoser.codyssey.bifroest.example.customers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerRepository {
  private Map<String, Customer> customers = new HashMap<>();

  public void insert(Customer customer) {
    customers.put(customer.getUuid(), customer);
  }

  public Customer get(String uuid) {
    return customers.get(uuid);
  }

  public Collection<Customer> getAll() {
    return customers.values();
  }

  public Collection<Customer> findByName(String name) {
    return customers.values().stream().filter(c -> c.firstName.equals(name)).toList();
  }
}
