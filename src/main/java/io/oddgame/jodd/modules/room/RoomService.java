package io.oddgame.jodd.modules.room;

import java.util.List;

public interface RoomService {
    String createRoom(String name, String country, String host, int size);

    List<Room> getRooms(String country);

    Room getRoom(String id);
}
