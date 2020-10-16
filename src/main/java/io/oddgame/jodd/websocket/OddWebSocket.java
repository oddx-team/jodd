package io.oddgame.jodd.websocket;

import io.oddgame.jodd.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class OddWebSocket extends WebSocketServer {
    private final Map<String, Collection<WebSocket>> rooms = new HashMap<>();

    private OddWebSocket(final int port) {
        super(new InetSocketAddress("localhost", port));
    }

    public static OddWebSocket port(final int port) {
        return new OddWebSocket(port);
    }

    private String getConnName(WebSocket conn) {
        var senderName = "system";
        if (conn != null) {
            val attachment = (Attachment) conn.getAttachment();
            senderName = attachment.getNickname();
        }
        return senderName;
    }

    private void doBroadcast(WebSocket sender, String topic, String text) {
        val msg = topic + ":" + getConnName(sender) + ":" + text;
        broadcast(msg);
    }

    private void doBroadcastRoom(WebSocket sender, String topic, String text, String roomName) {
        val msg = topic + ":" + getConnName(sender) + ":" + text;
        broadcast(msg, rooms.get(roomName));
    }

    private void addToRoom(String roomName, WebSocket webSocket) {
        val attachment = (Attachment) webSocket.getAttachment();
        if (attachment.getInRoom() != null && attachment.getInRoom().equals(roomName)) return;

        val r = rooms.get(roomName) == null ? new ArrayList<WebSocket>() : rooms.get(roomName);
        r.add(webSocket);
        rooms.put(roomName, r);
        attachment.setInRoom(roomName);
        doBroadcastRoom(null, "chat/private", attachment.getNickname() + " joined room ", roomName);
    }

    private void removeFromRoom(String roomName, WebSocket webSocket) {
        val attachment = (Attachment) webSocket.getAttachment();
        if (attachment.getInRoom() != null && attachment.getInRoom().equals(roomName)) {
            val r = rooms.get(roomName);
            r.remove(webSocket);
            attachment.setInRoom(null);
            doBroadcastRoom(null, "chat/private", attachment.getNickname() + " has left room " + roomName, roomName);
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        log.info("New connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        log.info("Closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        log.info("Received message from " + conn.getRemoteSocketAddress() + ": " + message);
        val attachment = (Attachment) conn.getAttachment();

        val messageArr = message.split(":");
        if (messageArr.length < 2) {
            conn.send("Invalid message!");
            return;
        }
        val topic = messageArr[0];
        val data = messageArr[1];
        switch (topic) {
            case "message":
                if (attachment.getInRoom() == null) {
                    doBroadcast(conn, "chat/public", data);
                } else {
                    val room = attachment.getInRoom();
                    doBroadcastRoom(conn, "chat/private", data, room);
                }
                break;
            case "room/in":
                addToRoom(data, conn);
                break;
            case "room/out":
                removeFromRoom(data, conn);
                break;
            default:
                conn.send("Invalid message!");
                break;
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        log.error("An error occurred on connection " + conn.getRemoteSocketAddress() + ":" + ex, ex);
    }

    @Override
    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
        ServerHandshakeBuilder builder = super
                .onWebsocketHandshakeReceivedAsServer(conn, draft, request);
        val isProd = System.getProperty("isProd");
        if (isProd == null) {
            val attachment = Attachment.builder()
                    .userId("007").nickname("TuHuynh")
                    .build();
            conn.setAttachment(attachment);
            return builder;
        }

        if (!request.hasFieldValue("Cookie")) {
            throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Not accepted!");
        }

        val cookieHeader = request.getFieldValue("Cookie");
        val token = cookieHeader.replace("token=", "");
        try {
            val decoded = JWTUtils.validateJWT(token);
            val userId = decoded.getClaim("userid").asString();
            val nickname = decoded.getClaim("nickname").asString();
            val attachment = Attachment.builder()
                    .userId(userId).nickname(nickname)
                    .build();
            conn.setAttachment(attachment);
        } catch (Exception e) {
            throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, e.getMessage());
        }
        return builder;
    }

    @Override
    public void onStart() {
        log.info("Started Odd WebSocket Server");
    }
}
