package cfg.lms.gbs.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(schema = "gas_booking", name = "bookings")
@Data
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingid;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private GasAgencyEntity gasAgency;

    @Column
    private String cylinderType; // "Domestic" or "Commercial"

    @Column
    private String status; // "In Progress", "Confirmed", "Denied"

    @Column
    private LocalDate bookingDate;

    @Column
    private LocalDate deliveryDate;
    @Column(name = "delivery_location")
    private String deliveryLocation;

    @Column(name = "rejection_reason")
    private String rejectionReason;
    @Column(name = "delivery_status")
    private String deliveryStatus;

}
