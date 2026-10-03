package com.learning.websocket.server.chat;

/** Defines chat message publication and active-connection queries. */
interface ChatService {

  /** Publishes a message and returns it with the server-assigned timestamp. */
  ChatMessage publish(ChatMessage message);

  /** Returns the current number of tracked WebSocket sessions. */
  int openConnections();
}