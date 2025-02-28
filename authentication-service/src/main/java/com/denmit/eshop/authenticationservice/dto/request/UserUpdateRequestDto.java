package com.denmit.eshop.authenticationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class UserUpdateRequestDto {

    private static final String CURRENT_PASSWORD = "Enter current password";
    private static final String NEW_PASSWORD = "Enter new password";
    private static final String CONFIRM_PASSWORD = "Confirm new password";
    private static final String WRONG_SIZE_OF_PASSWORD = "Password shouldn't be less than 4 symbols";
    private static final String INVALID_PASSWORD = "Password should contains Latin letters";

    @NotBlank(message = CURRENT_PASSWORD)
    @Size(min = 4, message = WRONG_SIZE_OF_PASSWORD)
    @Pattern(regexp = "^[^А-Яа-я]*$", message = INVALID_PASSWORD)
    private String currentPassword;

    @NotBlank(message = NEW_PASSWORD)
    @Size(min = 4, message = WRONG_SIZE_OF_PASSWORD)
    @Pattern(regexp = "^[^А-Яа-я]*$", message = INVALID_PASSWORD)
    private String newPassword;

    @NotBlank(message = CONFIRM_PASSWORD)
    @Size(min = 4, message = WRONG_SIZE_OF_PASSWORD)
    @Pattern(regexp = "^[^А-Яа-я]*$", message = INVALID_PASSWORD)
    private String confirmPassword;
}
