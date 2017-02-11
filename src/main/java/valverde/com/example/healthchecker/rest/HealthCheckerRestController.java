package valverde.com.example.healthchecker.rest;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import valverde.com.example.healthchecker.dto.*;
import valverde.com.example.healthchecker.service.ApplicationService;
import valverde.com.example.healthchecker.service.HealthCheckerService;
import valverde.com.example.healthchecker.task.TaskTriggers;
import java.util.List;

@RestController
@CommonsLog
@RequestMapping("/healthchecker/rest")
public class HealthCheckerRestController {

    @GetMapping("/getreports")
    public ResponseEntity<List<HealthReportDTO>> getReports(@RequestParam("page") Integer pageNumber,
                                                            @RequestParam("size") Integer pageSize,
                                                            @RequestParam("sort") String sortByField) {
        try {
            PageRequest pageRequest = new PageRequest(pageNumber, pageSize, Sort.Direction.DESC, sortByField);
            List<HealthReportDTO> reports = healthCheckerService.getReportsForPage(pageRequest);
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problem with getting information about health reports " +
                    "on page: "+pageNumber+", with size: "+pageSize+", sortedBy: "+sortByField, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getreportsamount")
    public ResponseEntity<Long> getReportsAmount() {
        try {
            return new ResponseEntity<>(healthCheckerService.getReportAmount(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problem while getting information about amount of reports.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getstatuses")
    public ResponseEntity<List<AppHealthDTO>> getActualStatuses() {
        try {
            List<AppHealthDTO> dtos = healthCheckerService.getHealthStatusesFromApps();
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problem while getting actual statuses of appReports.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getdetails")
    public ResponseEntity<DetailsDTO> getAppDetails() {
        try {
            return new ResponseEntity<>(healthCheckerService.getDetails(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problem while getting applications details.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/wakeup")
    public ResponseEntity<String> wakeUp() {
        try {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problem while trying to wake up application.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save-app")
    public HttpStatus saveApp(@RequestBody ApplicationDTO applicationDTO) {
        try {
            applicationService.saveApplication(applicationDTO);
            return HttpStatus.OK;
        } catch (Exception e) {
            log.error("Problem while saving application "+applicationDTO.getName(), e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @PostMapping("/delete-app")
    public HttpStatus deleteApp(@RequestBody ApplicationDTO applicationDTO) {
        try {
            applicationService.deleteApplication(applicationDTO);
            return HttpStatus.OK;
        } catch (Exception e) {
            log.error("Problem while deleting application "+applicationDTO.getName(), e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Autowired
    public HealthCheckerRestController(HealthCheckerService healthCheckerService, ApplicationService applicationService) {
        this.healthCheckerService = healthCheckerService;
        this.applicationService = applicationService;
    }

    @Autowired
    private TaskTriggers task;

    private final HealthCheckerService healthCheckerService;

    private final ApplicationService applicationService;
}