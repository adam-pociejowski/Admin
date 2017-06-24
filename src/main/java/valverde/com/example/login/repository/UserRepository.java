package valverde.com.example.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import valverde.com.example.login.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}