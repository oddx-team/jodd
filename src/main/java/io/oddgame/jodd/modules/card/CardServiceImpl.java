package io.oddgame.jodd.modules.card;

import com.mongodb.client.MongoCollection;
import io.oddgame.jodd.factories.AppFactory;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.sample;
import static com.mongodb.client.model.Filters.eq;

public class CardServiceImpl implements CardService {
    private final MongoCollection<Card> cardCollection = AppFactory.getMongoDatabase()
            .getCollection("card", Card.class);

    @Override
    public List<Card> getCards(String language) {
        val cards = new ArrayList<Card>();
        cardCollection.find(eq("language", language)).forEach(cards::add);
        return cards;
    }

    @Override
    public List<Card> generateCards(String color, int size) {
        val cards = new ArrayList<Card>();
        cardCollection.aggregate(Arrays.asList(
            eq("color", color),
            sample(size)
        )).forEach(cards::add);
        return cards;
    }
}
