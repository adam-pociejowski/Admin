package valverde.com.example.healthchecker.util;

import valverde.com.example.healthchecker.dto.AppHealthDTO;
import valverde.com.example.healthchecker.entity.*;
import valverde.com.example.healthchecker.enums.HealthState;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static valverde.com.example.healthchecker.enums.HealthState.*;

public class HealthReportUtils {

    public static HealthState getOverallStatus(List<AppHealthDTO> dtos) {
        HealthState state = SUCCESS;
        for (AppHealthDTO dto : dtos) {
            if (dto.getState().equals(WARNING) && !state.equals(ERROR)) {
                state = WARNING;
            } else if (dto.getState().equals(ERROR)) {
                state = ERROR;
            }
        }
        return state;
    }
}