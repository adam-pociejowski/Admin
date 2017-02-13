package valverde.com.example.healthchecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import valverde.com.example.healthchecker.entity.HealthReport;
import valverde.com.example.healthchecker.enums.HealthState;

import java.util.List;

public interface HealthReportRepository extends JpaRepository<HealthReport, Long> {
    HealthReport findTop1ByOrderByIdDesc();

    List<HealthReport> findAllByOrderById();

    HealthReport findTop1ByStatusOrderByIdDesc(HealthState state);
}