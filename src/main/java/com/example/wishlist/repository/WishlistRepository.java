package com.example.wishlist.repository;

import com.example.wishlist.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    @Override
    List<Wishlist> findAllById(Iterable<Long> longs);
}
