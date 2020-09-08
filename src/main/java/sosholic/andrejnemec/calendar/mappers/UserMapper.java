package sosholic.andrejnemec.calendar.mappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sosholic.andrejnemec.calendar.entities.User;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserMapper {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public static UserMapper map(User user) {
        return new UserMapper(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoleList());
    }

    public static List<UserMapper> map(List<User> users) {
        return users.stream().map(UserMapper::map).collect(Collectors.toList());
    }

}
