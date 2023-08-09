package SocketSharedData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@AllArgsConstructor
@Getter
@Setter
public class ScheduleIMG implements Serializable
{
    private final byte[] _img;
    private final String _scheduleKey;
    private final String _extension;
}