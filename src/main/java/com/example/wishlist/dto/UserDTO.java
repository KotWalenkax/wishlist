package com.example.wishlist.dto;

import com.example.wishlist.validator.UniqueUser;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

    @NotBlank(message = "User`s name cannot be empty")
    @UniqueUser(message = "User`s name must be unique")
    private String username;

    @NotBlank(message = "User`s password cannot be empty")
    private String password;

}
