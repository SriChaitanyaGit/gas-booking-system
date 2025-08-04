package cfg.lms.gbs.dtos;

import lombok.Data;

@Data
public class BookingDTO {
    private int bookingid;
    private int customerid;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private int agencyid;
//    private String rejectionReason;
    private String agencyName;
    private String cylinderType;
    private String status;
    private String bookingDate;
    private String deliveryDate;
    private String deliveryLocation; 
    private String deliveryStatus; 
    
}
