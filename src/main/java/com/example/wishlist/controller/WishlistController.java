package com.example.wishlist.controller;

import com.example.wishlist.dto.WishlistAccessDTO;
import com.example.wishlist.dto.WishlistUserDTO;
import com.example.wishlist.model.User;
import com.example.wishlist.model.Wishlist;
import com.example.wishlist.service.UserService;
import com.example.wishlist.service.WishlistService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/wishlists")
@Slf4j
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;

    public WishlistController(WishlistService wishlistService, UserService userService) {
        this.wishlistService = wishlistService;
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getUsersWishLists(Model model) {

        User user = userService.getUserByUsername();
        List<Wishlist> wishlists = user.getWishlists();

        WishlistUserDTO wishlistDTO = new WishlistUserDTO();

        model.addAttribute("wishlists", wishlists);
        model.addAttribute("wishlistDTO", wishlistDTO);

        return "user-wishlists";
    }

    @PostMapping("/users/create")
    public String createWishList(@Valid @ModelAttribute("wishlistDTO") WishlistUserDTO wishlistDTO,
                                 BindingResult result) {

        if (result.hasErrors()) {
            return "user-wishlists";
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setName(wishlistDTO.getName());
        wishlist.setPassword(wishlistDTO.getPassword());

        wishlistService.saveWishlist(wishlist);

        User user = userService.getUserByUsername();
        user.addWishlist(wishlist);
        userService.saveUser(user);

        return "redirect:/wishlists/users";
    }

    @GetMapping("/accessible")
    public String getAccessibleWishLists(Model model) {

        User user = userService.getUserByUsername();
        List<Wishlist> wishlists = wishlistService.getAllAccessibleWishlistsByIds(user.getAccessibleWishlists());

        WishlistAccessDTO wishlistDTO = new WishlistAccessDTO();

        model.addAttribute("wishlistDTO", wishlistDTO);
        model.addAttribute("wishlists", wishlists);

        return "accessible-wishlists";
    }

    @PostMapping("/accessible/access")
    public String getAccessToWishlist(@Valid @ModelAttribute("wishlistDTO") WishlistAccessDTO wishlistDTO,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return "accessible-wishlists";
        }

        Wishlist wishlist = checkAccessToWishlist(wishlistDTO.getId(), wishlistDTO.getPassword());
        if (wishlist != null) {
            User user = userService.getUserByUsername();
            user.addAccessibleWishlist(wishlistDTO.getId());
            userService.saveUser(user);

            return "redirect:/wishlists/accessible";
        } else {
            return "redirect:/wishlists/accessible?error";
        }
    }

    @GetMapping("/users/{id}/delete")
    public String deleteUsersWishlist(@PathVariable(name = "id") Long wishlistId) {
        User user = userService.getUserByUsername();
        user.removeWishlist(wishlistId);

        wishlistService.deleteWishList(wishlistId);
        userService.saveUser(user);

        return "redirect:/wishlists/users";
    }

    @GetMapping("/accessible/{id}/delete")
    public String deleteAccessibleWishlist(@PathVariable(name = "id") Long wishlistId) {
        User user = userService.getUserByUsername();
        user.removeAccessibleWishlist(wishlistId);

        userService.saveUser(user);

        return "redirect:/wishlists/accessible";
    }

    @GetMapping("/users/{id}/edit")
    public String getEditWishlistPage(@PathVariable(name = "id") Long wishlistId,
                                      Model model) {
        Wishlist wishlist = wishlistService.getWishlistById(wishlistId);
        WishlistUserDTO wishlistDTO = new WishlistUserDTO();
        wishlistDTO.setId(wishlistId);

        model.addAttribute("wishlistDTO", wishlistDTO);
        model.addAttribute("oldName", wishlist.getName());

        return "edit-wishlist";
    }

    @PostMapping("/users/edit")
    public String editWishlist(@Valid @ModelAttribute("wishlistDTO") WishlistUserDTO wishlistDTO,
                               BindingResult result) {

        if (result.hasErrors()) {
            return "edit-wishlist";
        }

        Wishlist wishlist = wishlistService.getWishlistById(wishlistDTO.getId());
        wishlist.setName(wishlistDTO.getName());
        wishlist.setPassword(wishlistDTO.getPassword());

        wishlistService.saveWishlist(wishlist);

        return "redirect:/wishlists/users";
    }

    private Wishlist checkAccessToWishlist(Long id, String password) {
        if (wishlistService.isWishlistExists(id)) {
            Wishlist wishlist = wishlistService.getWishlistById(id);
            if (wishlist.getPassword().equals(password)) {
                return wishlist;
            }
        }

        return null;
    }
}
