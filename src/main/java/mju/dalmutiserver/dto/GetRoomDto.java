package mju.dalmutiserver.dto;

import lombok.*;
import mju.dalmutiserver.entity.User;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRoomDto {

    private Long id;
    private List<User> users;
    private String mode;
}
