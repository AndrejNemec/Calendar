package sosholic.andrejnemec.calendar.security.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosholic.andrejnemec.calendar.entities.User;
import sosholic.andrejnemec.calendar.repository.UserRepository;

@AllArgsConstructor
@Service
public class UserPrincipalService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    @Transactional
    public UserPrincipal loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(s);
        return new UserPrincipal(user);
    }
}