package com.learning.postgres.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true) // Enables Fluent API.
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Model {

  private Long id;
  private String name;
  private String email;
}
