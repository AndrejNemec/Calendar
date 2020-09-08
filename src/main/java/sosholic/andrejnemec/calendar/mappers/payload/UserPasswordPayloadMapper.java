package sosholic.andrejnemec.calendar.mappers.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPasswordPayloadMapper {

    private String password;
    private String passwordRepeat;

}
