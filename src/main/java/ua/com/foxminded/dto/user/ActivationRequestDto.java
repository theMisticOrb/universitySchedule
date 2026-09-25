package ua.com.foxminded.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import ua.com.foxminded.annotation.NotBlankCharArray;
import ua.com.foxminded.annotation.SizeCharArray;

public class ActivationRequestDto {

    @NotBlank(message = "{validation.email.blank}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    @NotBlankCharArray(message = "{validation.password.blank}")
    @SizeCharArray(min = 8, message = "{validation.password.size}")
    private char[] password;

    @NotBlankCharArray(message = "{validation.confirmPassword.blank}")
    private char[] confirmPassword;

    public ActivationRequestDto() {
    }

    public ActivationRequestDto(
            @NotBlank(message = "{validation.email.blank}") @Email(message = "{validation.email.invalid}") String email,
            char[] password, char[] confirmPassword) {
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

    public char[] getPassword() {
        return password;
    }

    public char[] getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(char[] confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

}
