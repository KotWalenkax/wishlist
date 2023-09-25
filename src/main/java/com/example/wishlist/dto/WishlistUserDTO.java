package com.example.wishlist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WishlistUserDTO {

    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Password cannot be empty")
    private String password;

}
