package io.oddgame.jodd.modules.card;

import com.jinyframework.core.AbstractRequestBinder.Context;
import com.jinyframework.core.AbstractRequestBinder.HttpResponse;
import io.oddgame.jodd.factories.ServiceFactory;
import lombok.val;

public class CardHandler {
    private static final CardService cardService = ServiceFactory.getCardService();

    public static HttpResponse getCards(Context ctx) {
        val language = ctx.pathParam("language");
        val cards = cardService.getCards(language);
        return HttpResponse.of(cards);
    }

    public static HttpResponse generateCards(Context ctx) {
        val color = ctx.pathParam("color");
        val size = Integer.parseInt(ctx.pathParam("size"));
        val cards = cardService.generateCards(color, size);
        return HttpResponse.of(cards);
    }
}
