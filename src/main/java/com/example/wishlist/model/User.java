package com.example.wishlist.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Column(name = "accessible_wishlists")
    private List<Long> accessibleWishlists = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Wishlist> wishlists = new ArrayList<>();

    public void addWishlist(Wishlist wishlist) {
        getWishlists().add(wishlist);
    }

    public void addAccessibleWishlist(Long id) {
        getAccessibleWishlists().add(id);
    }

    public void removeWishlist(Long id) {
        getWishlists().removeIf(wishlist -> wishlist.getId().equals(id));
    }

    public void removeAccessibleWishlist(Long id) {
        getAccessibleWishlists().remove(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
