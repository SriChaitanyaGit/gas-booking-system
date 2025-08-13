package cfg.lms.gbs.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
