package com.service.marketplace.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer experience;
    private Double rating;
    private String description;
    private String picture;
}
