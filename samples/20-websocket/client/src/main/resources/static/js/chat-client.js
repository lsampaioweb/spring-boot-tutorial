/**
 * Controls the WebSocket chat demo page.
 * Handles connection lifecycle and message send/receive events.
 */
(() => {
  const LOG_PREFIX = "[websocket-client]";

  let stompClient = null;
  let isConnecting = false;

  const rootElement = document.querySelector(".container");
  const elements = getDomElements();
  const texts = getRuntimeTexts(rootElement);

  initializeServerUrl(elements.serverUrlInput, rootElement.dataset.defaultServerUrl || "");
  setConnectedState(elements, texts, false);
  registerEventHandlers(elements, texts);

  function getDomElements() {
    return {
      serverUrlInput: document.getElementById("serverUrl"),
      usernameInput: document.getElementById("username"),
      messageInput: document.getElementById("message"),
      status: document.getElementById("status"),
      chatMessages: document.getElementById("chatMessages"),
      connectButton: document.getElementById("connectButton"),
      disconnectButton: document.getElementById("disconnectButton"),
      sendButton: document.getElementById("sendButton")
    };
  }

  function getRuntimeTexts(container) {
    return {
      statusConnected: container.dataset.statusConnected || "Connected",
      statusDisconnected: container.dataset.statusDisconnected || "Disconnected",
      socketOpened: container.dataset.runtimeSocketOpened || "Socket opened. You are now connected.",
      socketClosed: container.dataset.runtimeSocketClosed || "Socket closed.",
      connectionFailed: container.dataset.runtimeConnectionFailed || "Connection failed. Check server URL and try again.",
      sentAtFallback: container.dataset.runtimeSentAtNa || "n/a"
    };
  }

  function initializeServerUrl(serverUrlInput, defaultServerUrl) {
    if (defaultServerUrl) {
      serverUrlInput.value = defaultServerUrl;
    }
  }

  function registerEventHandlers(elements, texts) {
    elements.connectButton.addEventListener("click", () => connect(elements, texts));
    elements.disconnectButton.addEventListener("click", () => disconnect(elements, texts));
    elements.sendButton.addEventListener("click", () => send(elements));
    window.addEventListener("beforeunload", () => disconnect(elements, texts));
  }

  function setConnectedState(elements, texts, connected) {
    elements.connectButton.disabled = connected;
    elements.disconnectButton.disabled = !connected;
    elements.sendButton.disabled = !connected;
    elements.status.textContent = connected ? texts.statusConnected : texts.statusDisconnected;
  }

  function appendMessage(elements, text) {
    const item = document.createElement("li");

    item.textContent = text;
    elements.chatMessages.appendChild(item);
  }

  function resetConnectionState(elements, texts) {
    stompClient = null;
    isConnecting = false;
    setConnectedState(elements, texts, false);
  }

  function hasActiveConnection() {
    return stompClient !== null && (isConnecting || stompClient.connected === true);
  }

  function hasStaleClientReference() {
    return stompClient !== null && stompClient.connected !== true;
  }

  function connect(elements, texts) {
    if (hasActiveConnection()) {
      console.info(`${LOG_PREFIX} already connected`);

      return;
    }

    if (hasStaleClientReference()) {
      console.warn(`${LOG_PREFIX} stale client reference reset before reconnect`);
      stompClient = null;
    }

    const serverUrl = elements.serverUrlInput.value.trim();

    if (!serverUrl) {
      appendMessage(elements, texts.connectionFailed);
      console.warn(`${LOG_PREFIX} connection failed due to empty server URL`);

      return;
    }

    const socket = new SockJS(serverUrl);

    stompClient = Stomp.over(socket);
    isConnecting = true;
    stompClient.debug = null;

    socket.onclose = () => handleSocketClose(elements, texts);

    stompClient.connect(
      {},
      () => handleConnectSuccess(elements, texts, serverUrl),
      () => handleConnectFailure(elements, texts)
    );
  }

  function handleSocketClose(elements, texts) {
    if (hasStaleClientReference()) {
      console.warn(`${LOG_PREFIX} socket closed unexpectedly`);
      resetConnectionState(elements, texts);
    }
  }

  function handleConnectSuccess(elements, texts, serverUrl) {
    isConnecting = false;
    setConnectedState(elements, texts, true);
    appendMessage(elements, texts.socketOpened);
    console.info(`${LOG_PREFIX} connected to ${serverUrl}`);

    stompClient.subscribe("/topic/messages", (frame) => {
      const payload = JSON.parse(frame.body);
      const sentAt = payload.sentAt || texts.sentAtFallback;

      appendMessage(elements, `${payload.sender}: ${payload.content} (${sentAt})`);
      console.debug(`${LOG_PREFIX} message received from ${payload.sender}`);
    });
  }

  function handleConnectFailure(elements, texts) {
    appendMessage(elements, texts.connectionFailed);
    console.warn(`${LOG_PREFIX} connection failed`);
    resetConnectionState(elements, texts);
  }

  function disconnect(elements, texts) {
    if (stompClient === null) {
      resetConnectionState(elements, texts);

      return;
    }

    stompClient.disconnect(() => {
      appendMessage(elements, texts.socketClosed);
      console.info(`${LOG_PREFIX} disconnected`);
      resetConnectionState(elements, texts);
    });
  }

  function send(elements) {
    if (stompClient === null) {
      return;
    }

    const sender = elements.usernameInput.value.trim();
    const content = elements.messageInput.value.trim();

    if (!sender || !content) {
      console.warn(`${LOG_PREFIX} ignored empty sender/content`);

      return;
    }

    stompClient.send("/app/chat.send", {}, JSON.stringify({ sender, content }));
    console.debug(`${LOG_PREFIX} message sent by ${sender}`);
    elements.messageInput.value = "";
  }
})();
