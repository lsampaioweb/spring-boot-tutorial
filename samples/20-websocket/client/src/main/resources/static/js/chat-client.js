(() => {
  let stompClient = null;
  let isConnecting = false;
  const LOG_PREFIX = "[websocket-client]";

  const rootElement = document.querySelector(".container");
  const serverUrlInput = document.getElementById("serverUrl");
  const usernameInput = document.getElementById("username");
  const messageInput = document.getElementById("message");
  const status = document.getElementById("status");
  const chatMessages = document.getElementById("chatMessages");

  const connectButton = document.getElementById("connectButton");
  const disconnectButton = document.getElementById("disconnectButton");
  const sendButton = document.getElementById("sendButton");

  const statusConnectedText = rootElement.dataset.statusConnected || "Connected";
  const statusDisconnectedText = rootElement.dataset.statusDisconnected || "Disconnected";
  const socketOpenedText = rootElement.dataset.runtimeSocketOpened || "Socket opened. You are now connected.";
  const socketClosedText = rootElement.dataset.runtimeSocketClosed || "Socket closed.";
  const connectionFailedText = rootElement.dataset.runtimeConnectionFailed || "Connection failed. Check server URL and try again.";
  const sentAtFallbackText = rootElement.dataset.runtimeSentAtNa || "n/a";

  // Keep one default endpoint in server-side template and reuse it in JS.
  serverUrlInput.value = rootElement.dataset.defaultServerUrl || "http://localhost:8090/ws";

  const setConnected = (connected) => {
    // Keep UI state consistent with socket lifecycle.
    connectButton.disabled = connected;
    disconnectButton.disabled = !connected;
    sendButton.disabled = !connected;
    status.textContent = connected ? statusConnectedText : statusDisconnectedText;
  };

  const appendMessage = (text) => {
    const item = document.createElement("li");

    item.textContent = text;
    chatMessages.appendChild(item);
  };

  const resetConnectionState = () => {
    stompClient = null;
    isConnecting = false;
    setConnected(false);
  };

  const hasActiveConnection = () => {
    return stompClient !== null && (isConnecting || stompClient.connected === true);
  };

  const connect = () => {
    if (hasActiveConnection()) {
      console.info(`${LOG_PREFIX} already connected`);

      return;
    }

    if (stompClient !== null && stompClient.connected !== true) {
      console.warn(`${LOG_PREFIX} stale client reference reset before reconnect`);
      stompClient = null;
    }

    const socket = new SockJS(serverUrlInput.value.trim());

    stompClient = Stomp.over(socket);
    isConnecting = true;
    // Disable verbose STOMP frame logs in the browser console.
    stompClient.debug = null;

    socket.onclose = () => {
      if (stompClient !== null && stompClient.connected !== true) {
        console.warn(`${LOG_PREFIX} socket closed unexpectedly`);
        resetConnectionState();
      }
    };

    stompClient.connect({}, () => {
      isConnecting = false;
      setConnected(true);
      appendMessage(socketOpenedText);
      console.info(`${LOG_PREFIX} connected to ${serverUrlInput.value.trim()}`);

      stompClient.subscribe("/topic/messages", (frame) => {
        const payload = JSON.parse(frame.body);
        const sentAt = payload.sentAt || sentAtFallbackText;

        appendMessage(`${payload.sender}: ${payload.content} (${sentAt})`);
        console.debug(`${LOG_PREFIX} message received from ${payload.sender}`);
      });
    }, () => {
      appendMessage(connectionFailedText);
      console.warn(`${LOG_PREFIX} connection failed`);
      resetConnectionState();
    });
  };

  const disconnect = () => {
    if (stompClient !== null) {
      stompClient.disconnect(() => {
        appendMessage(socketClosedText);
        console.info(`${LOG_PREFIX} disconnected`);
        resetConnectionState();
      });

      return;
    }

    resetConnectionState();
  };

  const send = () => {
    if (stompClient === null) {
      return;
    }

    const sender = usernameInput.value.trim();
    const content = messageInput.value.trim();

    if (!sender || !content) {
      console.warn(`${LOG_PREFIX} ignored empty sender/content`);

      return;
    }

    stompClient.send("/app/chat.send", {}, JSON.stringify({ sender, content }));
    console.debug(`${LOG_PREFIX} message sent by ${sender}`);
    messageInput.value = "";
  };

  connectButton.addEventListener("click", connect);
  disconnectButton.addEventListener("click", disconnect);
  sendButton.addEventListener("click", send);

  window.addEventListener("beforeunload", disconnect);
})();
