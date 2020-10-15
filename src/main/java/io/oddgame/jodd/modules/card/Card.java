package io.oddgame.jodd.modules.card;

import io.oddgame.jodd.commons.BaseEntity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card extends BaseEntity {
    private String color;
    private String language;
    private String text;
    private int gaps;
}
