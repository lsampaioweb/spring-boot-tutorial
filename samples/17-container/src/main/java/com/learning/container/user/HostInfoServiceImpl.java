package com.learning.container.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Service;

import com.learning.container.i18n.LogMessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class HostInfoServiceImpl implements HostInfoService {

  private static final String LOG_HOST_INFO = "log.container.host.info";

  private final LogMessages logMessages;

  @Override
  public HostInfoResponse sayHello() {
    try {
      InetAddress localHost = InetAddress.getLocalHost();
      String hostName = localHost.getHostName();
      String hostAddress = localHost.getHostAddress();

      log.info(logMessages.get(LOG_HOST_INFO, hostName, hostAddress));

      return new HostInfoResponse(hostName, hostAddress);
    } catch (UnknownHostException e) {
      throw new IllegalStateException(e);
    }
  }
}
