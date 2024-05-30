package com.learning.lombok.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserEqualsAndHashCode {

  private String name;
  private int age;
}
