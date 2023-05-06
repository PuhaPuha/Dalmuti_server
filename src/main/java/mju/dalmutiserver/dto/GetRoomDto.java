package mju.dalmutiserver.dto;

import lombok.*;
import mju.dalmutiserver.entity.User;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRoomDto {

    private Long id;
    private ArrayList<User> users;
}
