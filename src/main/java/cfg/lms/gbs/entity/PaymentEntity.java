package cfg.lms.gbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(schema="gas_booking",name = "payments")
@Data
public class PaymentEntity {
	@Id
    private int paymentid;
	
	@Column
    private String paymentMode;
	
	@Column
    private Double amount;
	
	@Column
    private String paymentStatus;

    @OneToOne
    @JoinColumn(name = "booking_id" ,referencedColumnName = "bookingid")
    private BookingEntity booking;
}
