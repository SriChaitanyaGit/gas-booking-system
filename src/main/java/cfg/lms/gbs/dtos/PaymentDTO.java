package cfg.lms.gbs.dtos;

import lombok.Data;

@Data
public class PaymentDTO {
    private int paymentid;
    private String paymentMode;
    private Double amount;
    private String paymentStatus; // Pending, In Progress, Completed, Failed
    private int bookingid;
}
