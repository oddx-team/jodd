package io.oddgame.jodd.websocket;

import com.jinyframework.websocket.WebSocketServer;
import com.jinyframework.websocket.server.Socket;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class OddWebSocketServer {
    public static WebSocketServer port(int port) {
        val server = WebSocketServer.port(port);

        server.handshake(WSHandshake::handshake);

        server.onOpen(socket ->
                log.info("New connection: " + socket.getIdentify()));
        server.onClose((socket, code, reason) ->
                log.info("Closed connection: " + socket.getIdentify() + ", code: " + code + ", reason: " + reason));
        server.onError((socket, ex) ->
                log.error("Socket error: " + socket.getIdentify() + ", error: " + ex.getMessage(), ex));

        server.on("room/join", Socket::join); // Join room
        server.on("room/leave", Socket::leave); // Leave room

        server.on("chat/public", (socket, message) -> {
            server.emit("chat/public", message);
        });

        server.on("chat/private", (socket, message) -> {
            val messageArray = message.split(":");
            val roomName = messageArray[0];
            val messageData = message.replaceFirst(roomName + ":", "");
            server.emitRoom(roomName,
                    "room-chat",
                    roomName, socket.getIdentify(), messageData);
        });

        return server;
    }
}
