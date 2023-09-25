package com.example.wishlist.service;

import com.example.wishlist.model.Wishlist;
import com.example.wishlist.repository.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WishlistService {

    private final WishlistRepository wishListRepository;

    public WishlistService(WishlistRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public boolean isWishlistExists(Long id) {
        return wishListRepository.existsById(id);
    }

    public void saveWishlist(Wishlist wishList) {
        wishListRepository.save(wishList);
    }

    public Wishlist getWishlistById(Long id) {
        return wishListRepository.findById(id).orElseThrow();
    }

    public List<Wishlist> getAllAccessibleWishlistsByIds(Iterable<Long> ids) {
        return wishListRepository.findAllById(ids);
    }

    public void deleteWishList(long id) {
        wishListRepository.deleteById(id);
    }

}
