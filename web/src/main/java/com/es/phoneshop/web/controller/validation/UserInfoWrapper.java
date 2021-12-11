package com.es.phoneshop.web.controller.validation;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserInfoWrapper {
  @NotEmpty(message = "Required")
  private String firstName;

  @NotEmpty(message = "Required")
  private String lastName;

  @NotEmpty(message = "Required")
  private String address;

  @NotEmpty(message = "Required")
  private String phone;

  @Length(max = 254)
  private String comment;

  public UserInfoWrapper() {
  }

  public UserInfoWrapper(String firstName, String lastName, String address, String phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.phone = phone;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
