package com.denmit.eshop.feedbackservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class FeedbackRequestDto {

    private static final String RATE_FIELD_IS_EMPTY = "Rate field shouldn't be empty";
    private static final String WRONG_RATE = "Rate should be one of the following: terrible, bad, medium, good, great";
    private static final String WRONG_SIZE_OF_FEEDBACK = "Feedback shouldn't be more than 100 symbols";

    @NotEmpty(message = RATE_FIELD_IS_EMPTY)
    @Pattern(regexp = "terrible|bad|medium|good|great", message = WRONG_RATE)
    private String rate;

    @Size(max = 100, message = WRONG_SIZE_OF_FEEDBACK)
    private String text;
}
