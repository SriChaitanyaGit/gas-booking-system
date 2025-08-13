package cfg.lms.gbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(schema = "gas_booking", name = "admins")
@Data
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int adminid;

    @Column
    private String username; // Full Name

    @Column
    private String email;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private String password;

    @Column
    private String role; // Expected: "admin"
}
