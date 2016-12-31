package valverde.com.example.healthchecker.rest;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import valverde.com.example.healthchecker.dto.HealthDTO;
import valverde.com.example.healthchecker.dto.HealthReportDTO;
import valverde.com.example.healthchecker.enums.App;
import valverde.com.example.healthchecker.service.HealthCheckerService;
import java.util.List;

@RestController
@CommonsLog
@RequestMapping("/healthchecker/rest")
public class HealthCheckerRestController {

    @GetMapping("/getallreports")
    public ResponseEntity<List<HealthReportDTO>> getAllReports() {
        try {
            return new ResponseEntity<>(healthCheckerService.getAllHealthReports(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problem with getting information about all health reports.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getstatuses")
    public ResponseEntity<List<HealthDTO>> getActualStatuses() {
        try {
            List<HealthDTO> dtos = healthCheckerService.getHealthStatusesFromApps();
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problem while getting actual statuses of apps.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getlastreport")
    public ResponseEntity<HealthReportDTO> getLastReport() {
        try {
            HealthReportDTO dto = healthCheckerService.getLastHealthReportAsDTO();
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problem while getting last report.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getapps")
    public ResponseEntity<List<App>> getApps() {
        try {
            List<App> apps = healthCheckerService.getApps();
            return new ResponseEntity<>(apps, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problem while getting apps.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    public HealthCheckerRestController(HealthCheckerService healthCheckerService) {
        this.healthCheckerService = healthCheckerService;
    }

    private final HealthCheckerService healthCheckerService;
}