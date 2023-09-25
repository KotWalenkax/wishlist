package com.example.wishlist.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "wishes")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wish wish = (Wish) o;
        return Objects.equals(id, wish.id) && Objects.equals(description, wish.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }
}
