package cfg.lms.gbs.controller;

import lombok.Data;

@Data
public class ResponseData {
    private String status;
    private Object data;
    private String message;
}
