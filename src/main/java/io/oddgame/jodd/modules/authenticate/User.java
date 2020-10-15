package io.oddgame.jodd.modules.authenticate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private ObjectId id;
    private String username;
    private String password;
    private String email;
}
