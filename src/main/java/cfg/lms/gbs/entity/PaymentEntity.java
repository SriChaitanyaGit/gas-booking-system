package cfg.lms.gbs.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(schema = "gas_booking", name = "payments")
@Data
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentid;

    @Column
    private String paymentMode;

    @Column
    private Double amount;

    @Column
    private String paymentStatus; // Pending, In Progress, Completed, Failed

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "bookingid")
    private BookingEntity booking;
}
