package cfg.lms.gbs.dtos;

import lombok.Data;

@Data
public class CustomerDTO {
    private int id;
    private String name;
    private String email;
    private String phone;
}