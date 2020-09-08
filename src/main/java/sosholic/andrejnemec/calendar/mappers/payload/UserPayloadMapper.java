package sosholic.andrejnemec.calendar.mappers.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sosholic.andrejnemec.calendar.entities.User;
import sosholic.andrejnemec.calendar.mappers.exceptions.BadPasswordException;
import sosholic.andrejnemec.calendar.mappers.exceptions.BadUsernameException;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPayloadMapper {

    private String username;

    private String password;
    private String passwordRepeat;

    private String firstName;
    private String lastName;

    public static User map(UserPayloadMapper userPayloadMapper) throws BadPasswordException, BadUsernameException {

        if (userPayloadMapper.username.length() > 19 || userPayloadMapper.username.length() < 3) throw new BadUsernameException("The username The password must be greater than 3 and less than 19");
        if (userPayloadMapper.password.length() < 6 || userPayloadMapper.password.length() > 25) throw new BadPasswordException("The password must be greater than 6 and less than 25");
        if (!userPayloadMapper.password.equals(userPayloadMapper.passwordRepeat)) throw new BadPasswordException("The passwords do not match!");

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        String password = bcrypt.encode(userPayloadMapper.password);

        return new User(0, userPayloadMapper.username, password, userPayloadMapper.firstName, userPayloadMapper.lastName, "USER");
    }

}
