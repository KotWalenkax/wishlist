package com.example.wishlist.service;

import com.example.wishlist.model.Wishlist;
import com.example.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class WishListServiceIntegrationTest {

    @Autowired
    WishlistRepository wishlistRepository;
    @Autowired
    WishlistService wishlistService;

    @AfterEach
    public void clearTable() {
        wishlistRepository.deleteAll();
    }

    @Test
    public void saveWishlistTest() {
        Wishlist wishlist = new Wishlist();
        wishlist.setId(1L);
        wishlist.setPassword("password");
        wishlist.setName("wishlistTest");

        wishlistService.saveWishlist(wishlist);

        Optional<Wishlist> wishlistTest = wishlistRepository.findById(1L);

        Assertions.assertTrue(wishlistTest.isPresent());
        Assertions.assertEquals(wishlist, wishlistTest.get());
    }

    @Test
    public void deleteWishlistTest() {
        Wishlist wishlist = createAndSaveWishlist(2L);

        wishlistService.deleteWishList(wishlist.getId());

        Assertions.assertFalse(wishlistRepository.existsById(wishlist.getId()));
    }

    @Test
    public void getAllAccessibleWishlistsByIdsTest() {
        List<Wishlist> wishlists = new ArrayList<>();

        Wishlist wishlist = createAndSaveWishlist(3L);
        Wishlist wishlist2 = createAndSaveWishlist(4L);
        createAndSaveWishlist(5L);
        wishlists.add(wishlist);
        wishlists.add(wishlist2);

        List<Long> ids = List.of(wishlist.getId(), wishlist2.getId());
        List<Wishlist> wishlistTest = wishlistService.getAllAccessibleWishlistsByIds(ids);

        Assertions.assertEquals(2, wishlistTest.size());
        Assertions.assertArrayEquals(wishlists.toArray(), wishlistTest.toArray());
    }

    private Wishlist createAndSaveWishlist(Long id) {
        Wishlist wishlist = new Wishlist();
        wishlist.setId(id);
        wishlist.setPassword("password" + id);
        wishlist.setName("wishlistTest" + id);

        wishlistRepository.save(wishlist);
        return wishlist;
    }

}
