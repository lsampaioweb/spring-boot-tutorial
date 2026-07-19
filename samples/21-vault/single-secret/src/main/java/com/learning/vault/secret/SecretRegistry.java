package com.learning.vault.secret;

public interface SecretRegistry {

  String get(String key);
}