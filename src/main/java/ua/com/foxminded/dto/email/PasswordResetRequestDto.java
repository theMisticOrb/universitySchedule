package ua.com.foxminded.dto.email;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class PasswordResetRequestDto {
    
    @NotBlank
    @Email
    private String email;

    public PasswordResetRequestDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
