package com.learning.traefik;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class ComposeIngressGovernanceTest {

  @Test
  void dockerCompose_whenUsingCanonicalIngress_shouldDeclareTraefikOnlyContract() throws IOException {
    Path composeFile = Path.of("docker-compose.yml");
    String compose = Files.readString(composeFile);

    assertThat(compose).contains("traefik.enable=true");
    assertThat(compose).contains("traefik.http.routers.app.rule=Host(`app.localhost`)");
    assertThat(compose).contains("traefik.http.services.app.loadbalancer.server.port=${SERVER_PORT:-9443}");
    assertThat(compose).contains("tutorial-network:");
    assertThat(compose).contains("cpus: \"1.00\"");
    assertThat(compose).contains("mem_limit: \"768m\"");
    assertThat(compose).contains("mem_reservation: \"256m\"");
    assertThat(compose).doesNotContain("ports:");
  }
}