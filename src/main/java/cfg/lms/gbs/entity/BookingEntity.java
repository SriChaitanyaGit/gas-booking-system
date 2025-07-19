package cfg.lms.gbs.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(schema="gas_booking",name = "bookings")
@Data
public class BookingEntity {
		@Id
	    private int bookingid;
		@Column
	    private String gasType;
		@Column
	    private LocalDate bookingDate;
		@Column
	    private String status;

	    @ManyToOne
	    @JoinColumn(name = "customer_id",referencedColumnName = "id")
	    private CustomerEntity customer;

	    @ManyToOne
	    @JoinColumn(name = "agency_id" ,referencedColumnName = "gasid")
	    private GasAgencyEntity gasAgency;

	    @OneToOne(mappedBy = "booking")
	    
	    private PaymentEntity payment;
}
