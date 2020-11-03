package io.oddgame.jodd.websocket;

import com.jinyframework.websocket.WebSocketServer;
import com.jinyframework.websocket.server.Socket;
import lombok.val;

public class OddWebSocketServer {
    public static WebSocketServer port(int port) {
        val server = WebSocketServer.port(port);

        server.handshake(WSHandshake::handshake);

        server.onOpen(socket ->
                System.out.println("New connection: " + socket.getIdentify()));
        server.onClose((socket, code, reason) ->
                System.out.println("Closed connection: " + socket.getIdentify() + ", code: " + code + ", reason: " + reason));
        server.onError((socket, ex) ->
                System.out.println("Socket error: " + socket.getIdentify() + ", error: " + ex.getMessage()));

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
