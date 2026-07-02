package ua.com.foxminded.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ActivationRequestDto {

    @NotBlank(message = "{validation.email.blank}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    @NotBlank(message = "{validation.password.blank}")
    @Size(min = 8, message = "{validation.password.size}")
    private String password;

    @NotBlank(message = "{validation.confirmPassword.blank}")
    private String confirmPassword;

    public ActivationRequestDto() {
    }

    public ActivationRequestDto(
            @NotBlank(message = "{validation.email.blank}") @Email(message = "{validation.email.invalid}") String email,
            @NotBlank(message = "{validation.password.blank}") @Size(min = 8, message = "{validation.password.size}") String password,
            @NotBlank(message = "{validation.confirmPassword.blank}") String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
