package com.learning.lombok.entity;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserData {

  @NonNull
  private String name;
  private int age;
}
