package io.oddgame.jodd.factories;

import io.oddgame.jodd.modules.authenticate.AuthenticateService;
import io.oddgame.jodd.modules.authenticate.AuthenticateServiceImpl;
import io.oddgame.jodd.modules.card.CardService;
import io.oddgame.jodd.modules.card.CardServiceImpl;
import io.oddgame.jodd.modules.chat.ChatService;
import io.oddgame.jodd.modules.chat.ChatServiceImpl;
import io.oddgame.jodd.modules.room.RoomService;
import io.oddgame.jodd.modules.room.RoomServiceImpl;
import lombok.Setter;

@Setter
public class ServiceFactory {
    private static AuthenticateService authenticateService;
    private static RoomService roomService;
    private static CardService cardService;
    private static ChatService chatService;

    public static AuthenticateService getAuthenticateService() {
        if (authenticateService == null) {
            authenticateService = new AuthenticateServiceImpl();
        }
        return authenticateService;
    }

    public static RoomService getRoomService() {
        if (roomService == null) {
            roomService = new RoomServiceImpl();
        }
        return roomService;
    }

    public static CardService getCardService() {
        if (cardService == null) {
            cardService = new CardServiceImpl();
        }
        return cardService;
    }

    public static ChatService getChatService() {
        if (chatService == null) {
            chatService = new ChatServiceImpl();
        }
        return chatService;
    }
}
