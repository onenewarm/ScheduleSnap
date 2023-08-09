package api.mentalotus.Domain;

import lombok.*;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadImg {
    @Id
    private String _id;
}
