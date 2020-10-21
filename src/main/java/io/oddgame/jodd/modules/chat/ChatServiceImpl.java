package io.oddgame.jodd.modules.chat;

import com.mongodb.client.MongoCollection;
import io.oddgame.jodd.factories.AppFactory;
import lombok.val;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatServiceImpl implements ChatService {
    private final MongoCollection<Chat> chatCollection = AppFactory.getMongoDatabase()
            .getCollection("chat", Chat.class);

    @Override
    public void createChat(String nickname, String message) {
        val currentUnixTime = new Date().getTime();
        val newChat = Chat.builder()
                .nickname(nickname).message(message).time(currentUnixTime)
                .build();
        chatCollection.insertOne(newChat);
    }

    @Override
    public List<Chat> getChats() {
        val chats = new ArrayList<Chat>();
        chatCollection.find().forEach(chats::add);
        return chats;
    }
}
