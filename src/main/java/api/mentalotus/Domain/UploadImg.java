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
    private int _status; // 0 : 에러상태, 1 : 이미지 업로드 완료, 2 : clova api 진행중, 3 : chat-gpt api 진행중, 4 : 완료

    public UploadImg(int status){
        this._status = status;
    }
}
