package valverde.com.example.login.service;

import org.springframework.stereotype.Service;
import valverde.com.example.login.entity.User;
import valverde.com.example.login.repository.UserRepository;

@Service
public class UserService {

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;
}
