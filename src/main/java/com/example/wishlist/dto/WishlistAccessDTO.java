package com.example.wishlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WishlistAccessDTO {

    @NotNull(message = "ID cannot be empty")
    private Long id;

    @NotBlank(message = "Password cannot be empty")
    private String password;

}
