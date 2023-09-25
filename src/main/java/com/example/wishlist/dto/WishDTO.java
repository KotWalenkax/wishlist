package com.example.wishlist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WishDTO {

    private Long id;

    @NotBlank(message = "Description cannot be empty")
    private String description;

}
