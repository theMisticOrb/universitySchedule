package ua.com.foxminded.dto.email;

import ua.com.foxminded.annotation.NotBlankCharArray;
import ua.com.foxminded.annotation.SizeCharArray;

public class PasswordResetDto {

    @NotBlankCharArray
    @SizeCharArray(min = 8, message = "{validation.password.size}")
    private char[] password;

    @NotBlankCharArray
    private char[] confirmPassword;

    public PasswordResetDto() {
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
