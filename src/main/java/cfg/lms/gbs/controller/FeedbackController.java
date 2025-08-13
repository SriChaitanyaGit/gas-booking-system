package cfg.lms.gbs.controller;

import cfg.lms.gbs.dtos.FeedbackDTO;
import cfg.lms.gbs.entity.FeedbackEntity;
import cfg.lms.gbs.service.FeedbackService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
@CrossOrigin

public class FeedbackController {

    private final FeedbackService feedbackService;
    

    public FeedbackController(FeedbackService feedbackService) {
		super();
		this.feedbackService = feedbackService;
	}


	@PostMapping("/feedback")
    public ResponseEntity<?> submitFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        try {
            feedbackService.saveFeedback(feedbackDTO);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
