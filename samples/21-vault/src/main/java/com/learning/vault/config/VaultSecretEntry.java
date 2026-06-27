package com.learning.vault.config;

public record VaultSecretEntry(
    String path,
    String key) {
}
