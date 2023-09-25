package com.example.wishlist.service;

import com.example.wishlist.model.Wish;
import com.example.wishlist.repository.WishRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class WishServiceIntegrationTest {

    @Autowired
    WishRepository wishRepository;
    @Autowired
    WishService wishService;

    @AfterEach
    public void clearTable() {
        wishRepository.deleteAll();
    }

    @Test
    public void saveWishTest() {
        Wish wish = new Wish();
        wish.setId(1L);
        wish.setDescription("wishTest");

        wishService.saveWish(wish);

        Optional<Wish> wishTest = wishRepository.findById(wish.getId());

        Assertions.assertTrue(wishTest.isPresent());
        Assertions.assertEquals(wish, wishTest.get());
    }

    @Test
    public void deleteWishTest() {
        Wish wish = new Wish();
        wish.setId(1L);
        wish.setDescription("wishTest");

        wishRepository.save(wish);

        wishService.deleteWish(wish.getId());

        Assertions.assertFalse(wishRepository.existsById(wish.getId()));
    }


}
