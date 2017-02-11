package valverde.com.example.healthchecker.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "application")
@Data
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_seq_gen")
    @SequenceGenerator(name = "application_seq_gen", sequenceName = "application_id_seq")
    private Long id;

    private String name;

    private String host;

    private Boolean active;

    @OneToMany(mappedBy = "application")
    private List<HealthAppReport> appReports;

}