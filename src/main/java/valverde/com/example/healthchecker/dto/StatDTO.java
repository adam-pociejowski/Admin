package valverde.com.example.healthchecker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import valverde.com.example.healthchecker.entity.HealthStat;

@Data
@NoArgsConstructor
public class StatDTO {

    private String statName;

    private Object stat;

    public StatDTO(HealthStat healthStat) {
        this.statName = healthStat.getStatName();
        this.stat = healthStat.getStat();
    }
}