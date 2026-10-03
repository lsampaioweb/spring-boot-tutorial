package com.learning.websocket.server.chat;

/** Reports the number of currently open chat WebSocket connections. */
public record ChatConnectionStatusResponse(int openConnections) {
}
