package cfg.lms.gbs.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(schema = "gas_booking", name = "gas_agencies")
@Data
public class GasAgencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gasid;

    @Column
    private String name;

    @Column
    private String location;

    @Column
    private int domesticCylinders;

    @Column
    private int commercialCylinders;

    @OneToMany(mappedBy = "gasAgency")
    private List<BookingEntity> bookings;
}
