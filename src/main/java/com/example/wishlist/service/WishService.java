package com.example.wishlist.service;

import com.example.wishlist.model.Wish;
import com.example.wishlist.repository.WishRepository;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public void saveWish(Wish wish) {
        wishRepository.save(wish);
    }

    public void deleteWish(long id) {
        wishRepository.deleteById(id);
    }

    public Wish getWishById(long id) {
        return wishRepository.findById(id).orElseThrow();
    }
}
