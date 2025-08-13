package cfg.lms.gbs.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
	private Long customerId;
    private String name;
    private String email;
    private String feedbackText;
}
