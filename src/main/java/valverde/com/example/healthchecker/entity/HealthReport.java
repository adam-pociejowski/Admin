package valverde.com.example.healthchecker.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import valverde.com.example.healthchecker.enums.HealthState;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "health_report")
@Data
public class HealthReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "health_report_seq_gen")
    @SequenceGenerator(name = "health_report_seq_gen", sequenceName = "health_report_id_seq")
    private Long id;

    @Enumerated(EnumType.STRING)
    private HealthState status;

    @Fetch(FetchMode.JOIN)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report", fetch = FetchType.EAGER)
    private List<HealthAppReport> appReports;

    @Temporal(TemporalType.TIMESTAMP)
    private Date reportDate;
}