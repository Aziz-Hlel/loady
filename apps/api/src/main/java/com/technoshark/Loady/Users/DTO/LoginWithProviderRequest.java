package com.technoshark.Loady.Users.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginWithProviderRequest {

    @NotBlank(message = "idToken is mandatory")
    @Size(max = 4096, message = "idToken is too large")
    private String idToken;

}
