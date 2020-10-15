package io.oddgame.jodd.modules.room;

import com.google.gson.Gson;
import com.jinyframework.core.AbstractRequestBinder.Context;
import com.jinyframework.core.AbstractRequestBinder.HttpResponse;
import io.oddgame.jodd.factories.AppFactory;
import io.oddgame.jodd.factories.ServiceFactory;
import io.oddgame.jodd.utils.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.val;

public class RoomHandler {
    private static final Gson gson = AppFactory.getGson();
    private static final RoomService roomService = ServiceFactory.getRoomService();

    public static HttpResponse createRoom(Context ctx) {
        val country = ctx.pathParam("country");
        val createRoomReq = gson.fromJson(ctx.getBody(), Room.class);
        val insertedId = roomService.createRoom(
                createRoomReq.getName(), country, ctx.dataParam("username"), createRoomReq.getSize());
        return HttpResponse.of(
                new MessageResponse("Created room: " + insertedId));
    }

    public static HttpResponse getRooms(Context ctx) {
        val country = ctx.pathParam("country");
        val rooms = roomService.getRooms(country);
        return HttpResponse.of(rooms);
    }

    public static HttpResponse getRoom(Context ctx) {
        val id = ctx.pathParam("id");
        val room = roomService.getRoom(id);
        return room != null ? HttpResponse.of(room) :
                HttpResponse.of("Room not found").status(404);
    }

    @AllArgsConstructor
    private static class CreateRoomRequest {
        private final String name;
        private final int size;
    }
}
