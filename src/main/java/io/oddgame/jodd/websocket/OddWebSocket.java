package io.oddgame.jodd.websocket;

import io.oddgame.jodd.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

@Slf4j
public class OddWebSocket extends WebSocketServer {
    private OddWebSocket(final int port) {
        super(new InetSocketAddress("localhost", port));
    }

    public static OddWebSocket port(final int port) {
        return new OddWebSocket(port);
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
        val attachment = (Attachment) conn.getAttachment();
        val messageToBroadcast = attachment.getNickname() + ": " + message;
        broadcast(messageToBroadcast);
        log.info("Received message from " + conn.getRemoteSocketAddress() + ": " + message);
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
