package valverde.com.example.healthchecker.service;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import valverde.com.example.healthchecker.dto.ApplicationDTO;
import valverde.com.example.healthchecker.entity.Application;
import valverde.com.example.healthchecker.repository.ApplicationRepository;
import java.util.List;

@Service
@CommonsLog
@Transactional
public class ApplicationService {

    public void saveApplication(ApplicationDTO applicationDTO) {
        Application application = new Application();
        if (applicationDTO.getId() != null) {
            application = applicationRepository.findOne(applicationDTO.getId());
        }
        application.setActive(applicationDTO.getActive());
        application.setHost(applicationDTO.getHost());
        application.setName(applicationDTO.getName());
        applicationRepository.save(application);
    }

    public void deleteApplication(ApplicationDTO applicationDTO) {
        if (applicationDTO.getId() == null) {
            log.error("Couldn't delete application because it doesn't exist in database.");
        } else {
            applicationRepository.delete(applicationDTO.getId());
        }
    }

    public Application findByName(String appName) {
        return applicationRepository.findByName(appName);
    }

    Application findById(Long id) {
        return applicationRepository.findOne(id);
    }

    List<Application> getActiveApplications() {
        return applicationRepository.findByActive(true);
    }

    List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    private final ApplicationRepository applicationRepository;
}