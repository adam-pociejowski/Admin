package valverde.com.example.healthchecker.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import valverde.com.example.healthchecker.enums.HealthState;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "health_app_report")
@Data
public class HealthAppReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "health_app_report_seq_gen")
    @SequenceGenerator(name = "health_app_report_seq_gen", sequenceName = "health_app_report_id_seq")
    private Long id;

    @ManyToOne
    private Application application;

    @Enumerated(EnumType.STRING)
    private HealthState status;

    @ManyToOne(cascade = CascadeType.ALL)
    private HealthReport report;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appReport", fetch = FetchType.EAGER)
    private List<HealthStat> stats;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appReport", fetch = FetchType.EAGER)
    private List<HealthMessage> messages;
}