package ua.com.foxminded.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import ua.com.foxminded.annotation.NotBlankCharArray;
import ua.com.foxminded.annotation.SizeCharArray;

public class RegistrationCompletionDto {

    @NotBlank
    @Email
    private String email;

    @NotBlankCharArray
    @SizeCharArray(min = 8, message = "{validation.password.size}")
    private char[] password;

    @NotBlankCharArray
    private char[] confirmPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public char[] getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(char[] confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
