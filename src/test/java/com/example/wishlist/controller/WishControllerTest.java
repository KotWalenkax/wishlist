package com.example.wishlist.controller;

import com.example.wishlist.dto.WishDTO;
import com.example.wishlist.model.Wish;
import com.example.wishlist.model.Wishlist;
import com.example.wishlist.service.WishService;
import com.example.wishlist.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = WishController.class)
@WithMockUser
public class WishControllerTest {

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private WishService wishService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllWishesTest() throws Exception {
        Wishlist wishlist = getTestWishlist();
        Wish wish = getTestWish();
        wishlist.addWish(wish);

        given(wishlistService.getWishlistById(wishlist.getId())).willReturn(wishlist);

        mockMvc.perform(get("/1/wishes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("wlId", equalTo(1L)))
                .andExpect(model().attribute("wishes", hasItems(wish)))
                .andExpect(model().attribute("wishDTO", notNullValue()))
                .andExpect(view().name("wishes"));

    }

    @Test
    public void createWishSuccessfulTest() throws Exception {
        Wishlist wishlist = getTestWishlist();
        Wish wish = getTestWish();

        given(wishlistService.getWishlistById(wishlist.getId())).willReturn(wishlist);

        mockMvc.perform(post("/1/wishes/create")
                        .with(csrf())
                        .sessionAttr("wish", wish)
                        .param("description", wish.getDescription()))
                .andExpect(redirectedUrl("/1/wishes"));
    }

    @Test
    public void createWishNullDescriptionTest() throws Exception {
        Wish wish = getTestWish();

        mockMvc.perform(post("/1/wishes/create")
                        .with(csrf())
                        .sessionAttr("wish", wish))
                .andExpect(status().isOk())
                .andExpect(view().name("wishes"));

        verify(wishlistService, never()).getWishlistById(anyLong());
        verify(wishlistService, never()).saveWishlist(any());
        verify(wishService, never()).saveWish(any());
    }

    @Test
    public void deleteWishTest() throws Exception {
        Wishlist wishlist = getTestWishlist();
        Wish wish = getTestWish();

        wishlist.addWish(wish);

        given(wishlistService.getWishlistById(wishlist.getId())).willReturn(wishlist);

        mockMvc.perform(get("/1/wishes/1/delete"))
                .andExpect(redirectedUrl("/1/wishes"));
    }

    @Test
    public void getEditPageTest() throws Exception {
        Wish wish = getTestWish();

        given(wishService.getWishById(wish.getId())).willReturn(wish);

        mockMvc.perform(get("/1/wishes/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("wishDTO", hasProperty("id", equalTo(1L))))
                .andExpect(model().attribute("wlId", equalTo(1L)))
                .andExpect(model().attribute("oldDescription", equalTo(wish.getDescription())))
                .andExpect(view().name("edit-wish"));

    }

    @Test
    public void editWishSuccessfulTest() throws Exception {
        Wish wish = getTestWish();

        WishDTO wishDTO = new WishDTO();
        wishDTO.setId(1L);
        wishDTO.setDescription("description");

        given(wishService.getWishById(wishDTO.getId())).willReturn(wish);

        mockMvc.perform(post("/1/wishes/edit")
                .with(csrf())
                .sessionAttr("wishDTO", wishDTO)
                .param("id", wishDTO.getId().toString())
                .param("description", wishDTO.getDescription()))
                .andExpect(redirectedUrl("/1/wishes"));
    }

    @Test
    public void editWishNullDescriptionTest() throws Exception {
        WishDTO wishDTO = new WishDTO();
        wishDTO.setId(1L);

        mockMvc.perform(post("/1/wishes/edit")
                        .with(csrf())
                        .sessionAttr("wishDTO", wishDTO)
                        .param("id", wishDTO.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-wish"));

        verify(wishService, never()).getWishById(anyLong());
    }


    private Wishlist getTestWishlist() {
        Wishlist wishlist = new Wishlist();
        wishlist.setId(1L);
        wishlist.setName("wlTest");
        wishlist.setPassword("password");

        return wishlist;
    }

    private Wish getTestWish() {
        Wish wish = new Wish();
        wish.setId(1L);
        wish.setDescription("testWish");

        return wish;
    }
}
