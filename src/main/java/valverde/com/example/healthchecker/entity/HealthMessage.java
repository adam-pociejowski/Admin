package valverde.com.example.healthchecker.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "health_message")
@Data
public class HealthMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "health_message_seq_gen")
    @SequenceGenerator(name = "health_message_seq_gen", sequenceName = "health_message_id_seq")
    private Long id;

    private String message;

    @ManyToOne(cascade = CascadeType.ALL)
    private HealthAppReport appReport;

}