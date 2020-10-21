package io.oddgame.jodd.modules.chat;

import java.util.List;

public interface ChatService {
    void createChat(String nickname, String message);
    List<Chat> getChats();
}
