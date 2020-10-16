package io.oddgame.jodd.modules.chat;

import com.mongodb.client.MongoCollection;
import io.oddgame.jodd.factories.AppFactory;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl implements ChatService {
    private final MongoCollection<Chat> chatCollection = AppFactory.getMongoDatabase()
            .getCollection("chat", Chat.class);

    @Override
    public List<Chat> getChats() {
        val chats = new ArrayList<Chat>();
        chatCollection.find().forEach(chats::add);
        return chats;
    }
}
