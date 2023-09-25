package com.example.wishlist.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "list_id")
    private List<Wish> wishes = new ArrayList<>();

    public void addWish(Wish wish) {
        getWishes().add(wish);
    }

    public void removeWish(Long id) {
        getWishes().removeIf(wish -> wish.getId().equals(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wishlist wishlist = (Wishlist) o;
        return Objects.equals(id, wishlist.id) && Objects.equals(name, wishlist.name) && Objects.equals(password, wishlist.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password);
    }
}
