package cfg.lms.gbs.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(schema="gas_booking" ,name = "gas_agencies")
@Data
public class GasAgencyEntity {

	@Id
    private int gasid;
	
	@Column
    private String name;
	
	@Column
    private String location;
	
	
	@OneToMany(mappedBy = "gasAgency")
    private List<BookingEntity> bookings;
    
}
