package SocketSharedData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class OcrResult implements Serializable {
    private String key;
    private String result;
}
