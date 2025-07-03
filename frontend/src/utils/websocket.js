// websocket.js
import { io } from "socket.io-client";

class WebSocketService {
  constructor() {
    this.socket = null;
    this.isConnected = false;
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 5;
    this.reconnectDelay = 2000; // Start with 2 seconds delay
    this.eventHandlers = new Map();
  }

  connect() {
    if (this.socket) {
      return;
    }

    const socketUrl = import.meta.env.VITE_WS_URL || "http://localhost:3000";

    this.socket = io(socketUrl, {
      auth: {
        token: localStorage.getItem("token"),
      },
      reconnection: true,
      reconnectionDelay: this.reconnectDelay,
      reconnectionDelayMax: 10000, // Maximum delay between reconnection attempts
      timeout: 10000, // Connection timeout
    });

    this.setupEventListeners();
  }

  setupEventListeners() {
    // Connection events
    this.socket.on("connect", () => {
      console.log("Connected to WebSocket Gateway");
      this.isConnected = true;
      this.reconnectAttempts = 0;
      this.emit("connectionStatus", { status: "connected" });
    });

    this.socket.on("disconnect", (reason) => {
      console.log("Disconnected from WebSocket Gateway:", reason);
      this.isConnected = false;
      this.emit("connectionStatus", { status: "disconnected", reason });
    });

    this.socket.on("connect_error", (error) => {
      console.error("Connection error:", error);
      this.handleReconnect();
      this.emit("connectionStatus", { status: "error", error: error.message });
    });

    // Custom event handlers
    this.socket.on("notification", (message) => {
      this.emit("notification", message);
    });

    // Add more custom event handlers here as needed
    this.socket.on("document_update", (data) => {
      this.emit("document_update", data);
    });

    this.socket.on("team_update", (data) => {
      this.emit("team_update", data);
    });
  }

  handleReconnect() {
    this.reconnectAttempts++;
    if (this.reconnectAttempts >= this.maxReconnectAttempts) {
      console.error("Max reconnection attempts reached");
      this.emit("connectionStatus", {
        status: "failed",
        reason: "Max reconnection attempts reached",
      });
      return;
    }

    const delay = Math.min(
      this.reconnectDelay * Math.pow(2, this.reconnectAttempts - 1),
      10000
    );

    setTimeout(() => {
      if (!this.isConnected) {
        console.log(
          `Attempting to reconnect... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`
        );
        this.socket.connect();
      }
    }, delay);
  }

  // Subscribe to events
  on(event, callback) {
    if (!this.eventHandlers.has(event)) {
      this.eventHandlers.set(event, new Set());
    }
    this.eventHandlers.get(event).add(callback);
  }

  // Unsubscribe from events
  off(event, callback) {
    if (this.eventHandlers.has(event)) {
      this.eventHandlers.get(event).delete(callback);
    }
  }

  // Emit events to subscribers
  emit(event, data) {
    if (this.eventHandlers.has(event)) {
      this.eventHandlers.get(event).forEach((callback) => callback(data));
    }
  }

  // Send message to server
  send(event, data) {
    if (this.isConnected && this.socket) {
      this.socket.emit(event, data);
    } else {
      console.warn("Cannot send message: Socket is not connected");
    }
  }

  // Disconnect the socket
  disconnect() {
    if (this.socket) {
      this.socket.disconnect();
      this.socket = null;
      this.isConnected = false;
      this.reconnectAttempts = 0;
    }
  }
}

// Create a singleton instance
const webSocketService = new WebSocketService();

export default webSocketService;
