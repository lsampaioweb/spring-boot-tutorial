package com.learning.traefik.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.learning.traefik.i18n.LogMessages;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
class HostInfoServiceImpl implements HostInfoService {

  private static final String HOST_INFO = "host.info";

  private final LogMessages logMessages;
  private final MessageSource messageSource;

  @Override
  public HelloResponse getHostInfo() throws UnknownHostException {
    InetAddress localHost = InetAddress.getLocalHost();

    String hostName = localHost.getHostName();
    String hostAddress = localHost.getHostAddress();

    String message = messageSource.getMessage(HOST_INFO, new Object[] { hostName, hostAddress },
        LocaleContextHolder.getLocale());

    log.info(logMessages.get(HOST_INFO, hostName, hostAddress));

    return new HelloResponse(message);
  }
}
