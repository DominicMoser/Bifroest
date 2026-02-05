package com.dmoser.codyssey.bifroest.forms;

public class FormElement {
  public String name;
  public String msg;
  public String value;

  public FormElement(String name, String msg) {
    this.name = name;
    this.msg = msg;
  }

  public String getName() {
    return name;
  }

  public String getMsg() {
    return msg;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
