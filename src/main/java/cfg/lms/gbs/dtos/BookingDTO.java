package cfg.lms.gbs.dtos;

import lombok.Data;

@Data
public class BookingDTO {
    private int bookingid;
    private String gasType;
    private String status;
    private String bookingDate;
    private int customerid;
    private int agencyid;
}