package io.oddgame.jodd.factories;

import io.oddgame.jodd.modules.authenticate.AuthenticateService;
import io.oddgame.jodd.modules.authenticate.AuthenticateServiceImpl;
import io.oddgame.jodd.modules.room.RoomService;
import io.oddgame.jodd.modules.room.RoomServiceImpl;
import lombok.Setter;

@Setter
public class ServiceFactory {
    private static AuthenticateService authenticateService;
    private static RoomService roomService;

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
}
