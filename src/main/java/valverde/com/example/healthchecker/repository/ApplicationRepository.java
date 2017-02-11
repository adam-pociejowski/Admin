package valverde.com.example.healthchecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import valverde.com.example.healthchecker.entity.Application;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByActive(Boolean active);

    Application findByName(String name);
}