package com.service.marketplace.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phoneNumber;

    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experience;

    @NotBlank
    private String description;


    //private String picture;
}
