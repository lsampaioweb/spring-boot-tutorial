package com.learning.thymeleaf.ops;

import org.springframework.stereotype.Service;

@Service
class OpsServiceImpl implements OpsService {

  @Override
  public String buildMessage(OperatingSystem operatingSystem) {
    return operatingSystem.getOs1() + " " + operatingSystem.getOs2() + " " + operatingSystem.getOs3();
  }
}