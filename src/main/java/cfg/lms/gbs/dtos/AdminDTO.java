package cfg.lms.gbs.dtos;

import lombok.Data;

@Data
public class AdminDTO {
    private int adminid;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String password;
    private String role;
}
	