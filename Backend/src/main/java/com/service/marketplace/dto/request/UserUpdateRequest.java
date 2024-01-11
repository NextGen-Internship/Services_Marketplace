package com.service.marketplace.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Size(min = 10, message = "Email must be at least 10 characters long")
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

    @Min(value = 0)
    @Max(value = 5)
    private double rating;

    //private String picture;
}
