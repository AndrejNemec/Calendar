package sosholic.andrejnemec.calendar.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosholic.andrejnemec.calendar.entities.User;
import sosholic.andrejnemec.calendar.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public User save(User user) {
        if (findUserByUsername(user.getUsername()) != null) return null;
        return userRepository.save(user);
    }

    public User findUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) return null;
        return user.get();
    }

    public User findUserByUsername(String username) {
        if (username.length() < 3|| username.length() > 19) return null;
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
