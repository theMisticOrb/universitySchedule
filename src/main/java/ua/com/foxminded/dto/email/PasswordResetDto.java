package ua.com.foxminded.dto.email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordResetDto {

    @NotBlank
    @Size(min = 8, message = "{validation.password.size}")
    private String password;

    @NotBlank
    private String confirmPassword;

    public PasswordResetDto() {
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
