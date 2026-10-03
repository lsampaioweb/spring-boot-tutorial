/**
 * Browser demo for WebSocket session lifecycle: connect, presence, admin kick, abuse burst.
 */
(() => {
  const LOG_PREFIX = "[websocket-lifecycle]";
  const BURST_COUNT = 8;

  let stompClient = null;
  let isConnecting = false;

  const rootElement = document.querySelector(".container");
  const elements = getDomElements();
  const texts = getRuntimeTexts(rootElement);

  initializeInputs(elements, rootElement);
  setConnectedState(elements, texts, false);
  registerEventHandlers(elements, texts);

  function getDomElements() {
    return {
      serverUrlInput: document.getElementById("serverUrl"),
      displayNameInput: document.getElementById("displayName"),
      status: document.getElementById("status"),
      connectButton: document.getElementById("connectButton"),
      disconnectButton: document.getElementById("disconnectButton"),
      pingButton: document.getElementById("pingButton"),
      burstButton: document.getElementById("burstButton"),
      adminUserInput: document.getElementById("adminUser"),
      adminPasswordInput: document.getElementById("adminPassword"),
      refreshSessionsButton: document.getElementById("refreshSessionsButton"),
      sessionsList: document.getElementById("sessionsList"),
      eventsList: document.getElementById("eventsList")
    };
  }

  function getRuntimeTexts(container) {
    return {
      statusConnected: container.dataset.statusConnected || "Connected",
      statusDisconnected: container.dataset.statusDisconnected || "Disconnected",
      socketOpened: container.dataset.runtimeSocketOpened || "Socket opened.",
      socketClosed: container.dataset.runtimeSocketClosed || "Socket closed.",
      connectionFailed: container.dataset.runtimeConnectionFailed || "Connection failed.",
      kicked: container.dataset.runtimeKicked || "Server closed this session.",
      kickLabel: "Kick"
    };
  }

  function initializeInputs(elements, container) {
    elements.serverUrlInput.value = container.dataset.defaultServerUrl || "";
    elements.sessionsApiUrl = container.dataset.sessionsApiUrl || "";
  }

  function registerEventHandlers(elements, texts) {
    elements.connectButton.addEventListener("click", () => connect(elements, texts));
    elements.disconnectButton.addEventListener("click", () => disconnect(elements, texts));
    elements.pingButton.addEventListener("click", () => send(elements, "/app/lifecycle.ping"));
    elements.burstButton.addEventListener("click", () => sendBurst(elements));
    elements.refreshSessionsButton.addEventListener("click", () => refreshSessions(elements));
    window.addEventListener("beforeunload", () => disconnect(elements, texts));
  }

  function setConnectedState(elements, texts, connected) {
    elements.connectButton.disabled = connected;
    elements.disconnectButton.disabled = !connected;
    elements.pingButton.disabled = !connected;
    elements.burstButton.disabled = !connected;
    elements.status.textContent = connected ? texts.statusConnected : texts.statusDisconnected;
  }

  function appendEvent(elements, text) {
    const item = document.createElement("li");
    item.textContent = text;
    elements.eventsList.prepend(item);
  }

  function resetConnectionState(elements, texts) {
    stompClient = null;
    isConnecting = false;
    setConnectedState(elements, texts, false);
  }

  function connect(elements, texts) {
    if (stompClient !== null) {
      return;
    }

    const serverUrl = elements.serverUrlInput.value.trim();
    const displayName = elements.displayNameInput.value.trim() || "anonymous";

    if (!serverUrl) {
      appendEvent(elements, texts.connectionFailed);
      return;
    }

    const socket = new SockJS(serverUrl);
    stompClient = Stomp.over(socket);
    isConnecting = true;
    stompClient.debug = null;

    socket.onclose = () => {
      if (stompClient !== null && stompClient.connected !== true) {
        appendEvent(elements, texts.kicked);
        resetConnectionState(elements, texts);
      }
    };

    stompClient.connect(
      { displayName },
      () => {
        isConnecting = false;
        setConnectedState(elements, texts, true);
        appendEvent(elements, texts.socketOpened);
        console.info(`${LOG_PREFIX} connected as ${displayName}`);

        stompClient.subscribe("/topic/presence", (frame) => {
          const event = JSON.parse(frame.body);
          appendEvent(
            elements,
            `${event.type}: ${event.displayName} (${event.sessionId}) active=${event.activeCount}`
          );
          refreshSessions(elements);
        });

        refreshSessions(elements);
      },
      () => {
        appendEvent(elements, texts.connectionFailed);
        resetConnectionState(elements, texts);
      }
    );
  }

  function disconnect(elements, texts) {
    if (stompClient === null) {
      resetConnectionState(elements, texts);
      return;
    }

    stompClient.disconnect(() => {
      appendEvent(elements, texts.socketClosed);
      resetConnectionState(elements, texts);
    });
  }

  function send(elements, destination) {
    if (stompClient === null || stompClient.connected !== true) {
      return;
    }

    stompClient.send(destination, {}, "{}");
  }

  function sendBurst(elements) {
    for (let index = 0; index < BURST_COUNT; index += 1) {
      send(elements, "/app/lifecycle.burst");
    }
    console.warn(`${LOG_PREFIX} sent burst of ${BURST_COUNT} messages`);
  }

  async function refreshSessions(elements) {
    const apiUrl = elements.sessionsApiUrl;
    if (!apiUrl) {
      return;
    }

    const username = elements.adminUserInput.value.trim();
    const password = elements.adminPasswordInput.value;
    const headers = {
      Authorization: `Basic ${btoa(`${username}:${password}`)}`
    };

    try {
      const response = await fetch(apiUrl, { headers });
      if (!response.ok) {
        appendEvent(elements, `Sessions API failed: HTTP ${response.status}`);
        return;
      }

      const sessions = await response.json();
      elements.sessionsList.innerHTML = "";

      sessions.forEach((session) => {
        const item = document.createElement("li");
        const label = document.createElement("span");
        label.textContent = `${session.displayName} — ${session.sessionId}`;

        const kickButton = document.createElement("button");
        kickButton.textContent = texts.kickLabel;
        kickButton.addEventListener("click", () => kickSession(elements, session.sessionId));

        item.appendChild(label);
        item.appendChild(kickButton);
        elements.sessionsList.appendChild(item);
      });
    } catch (error) {
      appendEvent(elements, `Sessions API error: ${error.message}`);
    }
  }

  async function kickSession(elements, sessionId) {
    const apiUrl = `${elements.sessionsApiUrl}/${encodeURIComponent(sessionId)}`;
    const username = elements.adminUserInput.value.trim();
    const password = elements.adminPasswordInput.value;

    const response = await fetch(apiUrl, {
      method: "DELETE",
      headers: {
        Authorization: `Basic ${btoa(`${username}:${password}`)}`
      }
    });

    appendEvent(elements, response.ok
      ? `Kick requested for ${sessionId}`
      : `Kick failed for ${sessionId}: HTTP ${response.status}`);

    refreshSessions(elements);
  }
})();
