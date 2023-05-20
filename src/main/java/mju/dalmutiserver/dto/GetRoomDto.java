package mju.dalmutiserver.dto;

import lombok.*;
import mju.dalmutiserver.entity.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRoomDto {

    private Long id;
    private User user;
}
