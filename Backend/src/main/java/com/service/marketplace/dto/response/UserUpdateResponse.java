package com.service.marketplace.dto.response;

import lombok.Data;

@Data
public class UserUpdateResponse {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer experience;
    private String description;
   // private String pictureUrl;
    private Double rating;

}
