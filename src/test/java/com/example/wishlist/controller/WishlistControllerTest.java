package com.example.wishlist.controller;

import com.example.wishlist.dto.WishlistAccessDTO;
import com.example.wishlist.dto.WishlistUserDTO;
import com.example.wishlist.model.User;
import com.example.wishlist.model.Wishlist;
import com.example.wishlist.service.UserService;
import com.example.wishlist.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = WishlistController.class)
@WithMockUser
public class WishlistControllerTest {

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUsersWishlistsTest() throws Exception {
        User user = new User();
        user.setUsername("usertest");
        user.setPassword("password");

        Wishlist wishlist1 = new Wishlist();
        wishlist1.setName("wishlistOneTest");
        wishlist1.setPassword("passwordOne");

        Wishlist wishlist2 = new Wishlist();
        wishlist1.setName("wishlistTwoTest");
        wishlist1.setPassword("passwordTwo");

        user.addWishlist(wishlist1);
        user.addWishlist(wishlist2);

        given(userService.getUserByUsername()).willReturn(user);

        mockMvc.perform(get("/wishlists/users"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("wishlists", hasItems(wishlist1, wishlist2)))
                .andExpect(model().attribute("wishlistDTO", notNullValue()))
                .andExpect(view().name("user-wishlists"));
    }

    @Test
    public void createWishlistSuccessfulTest() throws Exception {
        User user = getTestUser();
        Wishlist wishlist = getTestWishlist();

        given(userService.getUserByUsername()).willReturn(user);

        mockMvc.perform(post("/wishlists/users/create")
                    .with(csrf())
                    .sessionAttr("wishlist", wishlist)
                    .param("name", wishlist.getName())
                    .param("password", wishlist.getPassword()))
                .andExpect(redirectedUrl("/wishlists/users"));
    }

    @Test
    public void createWishlistWishNullNameTest() throws Exception {
        User user = getTestUser();
        Wishlist wishlist = getTestWishlist();

        mockMvc.perform(post("/wishlists/users/create")
                        .with(csrf())
                        .sessionAttr("wishlist", wishlist)
                        .param("password", wishlist.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("user-wishlists"));

        verify(userService, never()).getUserByUsername();
        verify(userService, never()).saveUser(any());
    }

    @Test
    public void createWishlistWishNullPasswordTest() throws Exception {
        User user = getTestUser();
        Wishlist wishlist = getTestWishlist();

        mockMvc.perform(post("/wishlists/users/create")
                        .with(csrf())
                        .sessionAttr("wishlist", wishlist)
                        .param("name", wishlist.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("user-wishlists"));

        verify(userService, never()).getUserByUsername();
        verify(userService, never()).saveUser(any());
    }

    @Test
    public void getAccessibleWishlistsTest() throws Exception {
        User user = new User();
        user.setUsername("usertest");
        user.setPassword("password");
        user.addAccessibleWishlist(1L);
        user.addAccessibleWishlist(2L);

        Wishlist wishlist1 = new Wishlist();
        wishlist1.setName("wishlistOneTest");
        wishlist1.setPassword("passwordOne");

        Wishlist wishlist2 = new Wishlist();
        wishlist1.setName("wishlistTwoTest");
        wishlist1.setPassword("passwordTwo");

        List<Wishlist> wishlists = new ArrayList<>();

        wishlists.add(wishlist1);
        wishlists.add(wishlist2);

        given(userService.getUserByUsername()).willReturn(user);
        given(wishlistService.getAllAccessibleWishlistsByIds(user.getAccessibleWishlists()))
                .willReturn(wishlists);

        mockMvc.perform(get("/wishlists/accessible"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("wishlists", hasItems(wishlist1, wishlist2)))
                .andExpect(view().name("accessible-wishlists"));

    }

    @Test
    public void getAccessToWishlistSuccessfulTest() throws Exception {
        User user = getTestUser();
        Wishlist wishlist = getTestWishlist();

        given(wishlistService.isWishlistExists(anyLong())).willReturn(true);
        given(wishlistService.getWishlistById(anyLong())).willReturn(wishlist);
        given(userService.getUserByUsername()).willReturn(user);

        mockMvc.perform(post("/wishlists/accessible/access")
                    .with(csrf())
                        .param("id", "1")
                        .param("password", "password"))
                .andExpect(redirectedUrl("/wishlists/accessible"));

    }

    @Test
    public void getAccessToWishlistWithDoesntIdExistTest() throws Exception {
        given(wishlistService.isWishlistExists(anyLong())).willReturn(false);

        mockMvc.perform(post("/wishlists/accessible/access")
                        .with(csrf())
                        .param("id", "1")
                        .param("password", "password"))
                .andExpect(redirectedUrl("/wishlists/accessible?error"));

        verify(wishlistService, never()).getWishlistById(anyLong());
        verify(userService, never()).getUserByUsername();
    }

    @Test
    public void getAccessToWishlistWithNullIdTest() throws Exception {
        WishlistAccessDTO wishlist = new WishlistAccessDTO();


        mockMvc.perform(post("/wishlists/accessible/access")
                        .with(csrf())
                        .sessionAttr("wishlistDTO", wishlist)
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("accessible-wishlists"));

        verify(wishlistService, never()).isWishlistExists(anyLong());
        verify(wishlistService, never()).getWishlistById(anyLong());
        verify(userService, never()).getUserByUsername();
    }

    @Test
    public void getAccessToWishlistWithIncorrectPasswordTest() throws Exception {
        Wishlist wishlist = getTestWishlist();

        given(wishlistService.isWishlistExists(wishlist.getId())).willReturn(true);
        given(wishlistService.getWishlistById(wishlist.getId())).willReturn(wishlist);

        mockMvc.perform(post("/wishlists/accessible/access")
                        .with(csrf())
                        .param("id", "1")
                        .param("password", "pass"))
                .andExpect(redirectedUrl("/wishlists/accessible?error"));

        verify(userService, never()).getUserByUsername();
    }

    @Test
    public void deleteUsersWishlistTest() throws Exception {
        User user = getTestUser();
        user.addAccessibleWishlist(1L);

        given(userService.getUserByUsername()).willReturn(user);

        mockMvc.perform(get("/wishlists/accessible/1/delete"))
                .andExpect(redirectedUrl("/wishlists/accessible"));
    }

    @Test
    public void getEditWishlistPage() throws Exception {
        Wishlist wishlist = getTestWishlist();

        given(wishlistService.getWishlistById(wishlist.getId())).willReturn(wishlist);

        mockMvc.perform(get("/wishlists/users/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("wishlistDTO", hasProperty("id", equalTo(1L))))
                .andExpect(model().attribute("oldName", equalTo(wishlist.getName())))
                .andExpect(view().name("edit-wishlist"));

    }

    @Test
    public void editWishlistSuccessfulTest() throws Exception {
        Wishlist wishlist = getTestWishlist();
        WishlistUserDTO wishlistDTO = new WishlistUserDTO();

        given(wishlistService.getWishlistById(wishlist.getId())).willReturn(wishlist);

        mockMvc.perform(post("/wishlists/users/edit")
                    .with(csrf())
                    .sessionAttr("wishlistDTO", wishlistDTO)
                    .param("id", wishlist.getId().toString())
                    .param("name", wishlist.getName())
                    .param("password", wishlist.getPassword()))
                .andExpect(redirectedUrl("/wishlists/users"));

    }

    @Test
    public void editWishlistNullNameTest() throws Exception {
        Wishlist wishlist = getTestWishlist();
        WishlistUserDTO wishlistDTO = new WishlistUserDTO();

        mockMvc.perform(post("/wishlists/users/edit")
                        .with(csrf())
                        .sessionAttr("wishlistDTO", wishlistDTO)
                        .param("id", wishlist.getId().toString())
                        .param("password", wishlist.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-wishlist"));

        verify(wishlistService, never()).getWishlistById(wishlist.getId());
    }

    @Test
    public void editWishlistNullPasswordTest() throws Exception {
        Wishlist wishlist = getTestWishlist();
        WishlistUserDTO wishlistDTO = new WishlistUserDTO();

        mockMvc.perform(post("/wishlists/users/edit")
                        .with(csrf())
                        .sessionAttr("wishlistDTO", wishlistDTO)
                        .param("id", wishlist.getId().toString())
                        .param("name", wishlist.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-wishlist"));

        verify(wishlistService, never()).getWishlistById(wishlist.getId());
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("usertest");
        user.setPassword("password");

        return user;
    }

    private Wishlist getTestWishlist() {
        Wishlist wishlist = new Wishlist();
        wishlist.setId(1L);
        wishlist.setName("wltest");
        wishlist.setPassword("password");

        return wishlist;
    }
}
