package valverde.com.example.healthchecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import valverde.com.example.healthchecker.entity.HealthReport;

public interface HealthReportRepository extends JpaRepository<HealthReport, Long> {
    HealthReport findTop1ByOrderByIdDesc();
}