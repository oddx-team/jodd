package io.oddgame.jodd.modules.authenticate;

import io.oddgame.jodd.commons.BaseEntity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;
}
