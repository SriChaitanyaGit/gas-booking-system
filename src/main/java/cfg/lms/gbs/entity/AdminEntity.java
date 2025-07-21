package cfg.lms.gbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(schema="gas_booking",name = "admins")
@Data
public class AdminEntity {
	@Id
    private int adminid;
	
	@Column
    private String username;
	
	@Column
    private String password;
}
