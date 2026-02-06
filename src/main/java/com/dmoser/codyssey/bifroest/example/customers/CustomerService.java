package com.dmoser.codyssey.bifroest.example.customers;

import com.dmoser.codyssey.bifroest.example.customers.dto.CustomerDTO;
import com.dmoser.codyssey.bifroest.example.customers.dto.NewCustomerDTO;
import java.util.List;
import java.util.Optional;

public class CustomerService {

  CustomerRepository repository = new CustomerRepository();

  public void addNew(NewCustomerDTO newCustomer) {
    Customer c = new Customer();
    c.firstName = stripQuotes(newCustomer.firstName());
    c.lastName = stripQuotes(newCustomer.lastName());
    c.uuid = Customer.nextUuid();
    repository.insert(c);
  }

  public List<CustomerDTO> list() {
    return repository.getAll().stream()
        .map(bo -> new CustomerDTO(bo.getUuid(), bo.getFirstName(), bo.getLastName()))
        .toList();
  }

  public void updateCustomer(CustomerDTO customerDTO) {
    Customer c = repository.get(customerDTO.uuid());

    if (!customerDTO.firstName().isEmpty()) {

      c.firstName = stripQuotes(customerDTO.firstName());
    }
    if (!customerDTO.lastName().isEmpty()) {
      c.lastName = stripQuotes(customerDTO.lastName());
    }
    // repository.insert(c);
  }

  private String stripQuotes(String value) {
    if (value == null) {
      return null;
    }
    if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
      return value.substring(1, value.length() - 1);
    }
    return value;
  }

  public CustomerDTO get(String uuid) {
    return Optional.of(repository.get(uuid))
        .map(bo -> new CustomerDTO(bo.getUuid(), bo.getFirstName(), bo.getLastName()))
        .orElseGet(() -> null);
  }

  public CustomerDTO findFirstByName(String name) {
    return repository.findByName(name).stream()
        .map(bo -> new CustomerDTO(bo.getUuid(), bo.getFirstName(), bo.getLastName()))
        .findFirst()
        .get();
  }

  public boolean has(String uuid) {
    return repository.get(uuid) != null;
  }
}
