package api.mentalotus.Domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("User")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String userKey;
    private String nickname;
    private String email;

}
