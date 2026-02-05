package com.dmoser.codyssey.bifroest.example.customers;

public class Customer {
  public static Integer uuidGen = 0;
  public String firstName;
  public String lastName;
  public String uuid;

  public static String nextUuid() {
    uuidGen++;
    return uuidGen.toString();
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
