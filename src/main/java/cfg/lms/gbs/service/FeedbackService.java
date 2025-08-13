package cfg.lms.gbs.service;

import cfg.lms.gbs.dtos.FeedbackDTO;
import cfg.lms.gbs.entity.FeedbackEntity;
import cfg.lms.gbs.repository.FeedbackRepository;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    

    public FeedbackService(FeedbackRepository feedbackRepository) {
		super();
		this.feedbackRepository = feedbackRepository;
	}


    public FeedbackEntity saveFeedback(FeedbackDTO dto) {
        FeedbackEntity feedback = new FeedbackEntity();
        feedback.setCustomerId(dto.getCustomerId());
        feedback.setName(dto.getName());
        feedback.setEmail(dto.getEmail());
        feedback.setFeedbackText(dto.getFeedbackText());
        return feedbackRepository.save(feedback);
    }
}

