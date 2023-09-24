package api.mentalotus.Domain;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Data
@Document("Schedule")
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    @Id
    private String id;
    private String userKey;
    private String title;
    private String comment;

    @Field("start")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private String start;

    @Field("end")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private String end;

    private String location;


}
