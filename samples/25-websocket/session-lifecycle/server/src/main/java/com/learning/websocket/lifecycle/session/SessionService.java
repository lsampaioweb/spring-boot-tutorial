package com.learning.websocket.lifecycle.session;

import java.util.List;

public interface SessionService {

  void onConnect(String sessionId, String displayName);

  void onDisconnect(String sessionId, PresenceType type);

  List<SessionResponse> listSessions();

  void forceDisconnect(String sessionId);

  void handleClientMessage(String sessionId);
}
