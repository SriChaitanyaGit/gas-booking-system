package cfg.lms.gbs.dtos;

import lombok.Data;

@Data
public class PaymentStatusUpdateDTO {
    private String paymentStatus;
    private String bookingStatus; // This will map to BookingEntity.status
}
