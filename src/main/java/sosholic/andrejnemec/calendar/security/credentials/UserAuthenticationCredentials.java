package sosholic.andrejnemec.calendar.security.credentials;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAuthenticationCredentials {

    private String username;
    private String password;

}
