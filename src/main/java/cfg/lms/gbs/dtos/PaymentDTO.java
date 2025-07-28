package cfg.lms.gbs.dtos;

import lombok.Data;

@Data
public class PaymentDTO {
    private int paymentid;
    private String paymentMode;
    private Double amount;
    private String paymentStatus;
    private int bookingid;
}
