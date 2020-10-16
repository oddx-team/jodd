package io.oddgame.jodd.modules.card;

import java.util.List;

public interface CardService {
    List<Card> getCards(String language);
    List<Card> generateCards(String color, int size);
}
