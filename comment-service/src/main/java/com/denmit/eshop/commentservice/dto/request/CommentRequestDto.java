package com.denmit.eshop.commentservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class CommentRequestDto {

    private static final String COMMENT_IS_EMPTY = "Comment shouldn't be empty";
    private static final String WRONG_SIZE_OF_COMMENT = "Comment shouldn't be more than 300 symbols";
    private static final String WRONG_COMMENT = "Comment should contain latin letters or figures";

    @NotEmpty(message = COMMENT_IS_EMPTY)
    @Pattern(regexp = "^[A-za-z0-9 ,.!]*$", message = WRONG_COMMENT)
    @Size(max = 300, message = WRONG_SIZE_OF_COMMENT)
    private String text;
}
