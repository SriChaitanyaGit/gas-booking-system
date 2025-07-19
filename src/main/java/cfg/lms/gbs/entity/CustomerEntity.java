package cfg.lms.gbs.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema="gas_booking",name = "customers")
@Data
public class CustomerEntity {


	    @Id
	    private int id;
	    @Column
	    private String name;
	    @Column
	    private String email;
	    @Column
	    private String phone;
	    
	    @OneToMany(mappedBy = "customer")
	    private List<BookingEntity> bookings;
	    
	}

