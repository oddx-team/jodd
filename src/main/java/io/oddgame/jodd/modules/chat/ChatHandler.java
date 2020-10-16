package io.oddgame.jodd.modules.chat;

import com.jinyframework.core.AbstractRequestBinder.Context;
import com.jinyframework.core.AbstractRequestBinder.HttpResponse;
import io.oddgame.jodd.factories.ServiceFactory;

public class ChatHandler {
    private static final ChatService chatService = ServiceFactory.getChatService();

    public static HttpResponse getChats(Context ctx) {
        return HttpResponse.of(chatService.getChats());
    }
}
