package valverde.com.example.healthchecker.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "health_stat")
@Data
public class HealthStat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "health_stat_seq_gen")
    @SequenceGenerator(name = "health_stat_seq_gen", sequenceName = "health_stat_id_seq")
    private Long id;

    private String statName;

    private String stat;

    @ManyToOne(cascade = CascadeType.ALL)
    private HealthAppReport appReport;
}