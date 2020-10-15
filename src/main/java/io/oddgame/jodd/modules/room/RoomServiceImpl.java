package io.oddgame.jodd.modules.room;

import com.mongodb.client.MongoCollection;
import io.oddgame.jodd.factories.AppFactory;
import io.oddgame.jodd.modules.room.Room.Status;
import lombok.val;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class RoomServiceImpl implements RoomService {
    private final MongoCollection<Room> roomCollection = AppFactory.getMongoDatabase()
            .getCollection("room", Room.class);

    @Override
    public String createRoom(String name, String country, String host, int size) {
        val newRoom = Room.builder()
                .name(name).country(country).host(host)
                .size(size).guess(0).status(Status.NOT_STARTED)
                .build();
        val inserted = roomCollection.insertOne(newRoom);
        return Objects.requireNonNull(inserted.getInsertedId()).toString();
    }

    @Override
    public List<Room> getRooms(String country) {
        val rooms = new ArrayList<Room>();
        roomCollection.find(eq("country", country)).forEach(rooms::add);
        return rooms;
    }

    @Override
    public Room getRoom(String id) {
        return roomCollection.find(eq("_id", new ObjectId(id))).first();
    }
}
