package com.learning.lombok.entity;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class UserBuilder {

  private String name;
  private int age;
}
