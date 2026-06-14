package com.learning.websocket.server.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.websocket.server.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/websocket")
@Slf4j
public class WebSocketStatusApi {

  private static final String LOG_WEBSOCKET_STATUS_REQUESTED = "log.websocket.status.requested";

  private final ConnectionTracker connectionTracker;
  private final LogMessages logMessages;

  public WebSocketStatusApi(ConnectionTracker connectionTracker, LogMessages logMessages) {
    this.connectionTracker = connectionTracker;
    this.logMessages = logMessages;
  }

  @GetMapping("/connections")
  public WebSocketConnectionStatusResponse openConnections() {
    log.debug(logMessages.get(LOG_WEBSOCKET_STATUS_REQUESTED, connectionTracker.getOpenConnections()));

    return new WebSocketConnectionStatusResponse(connectionTracker.getOpenConnections());
  }

}
