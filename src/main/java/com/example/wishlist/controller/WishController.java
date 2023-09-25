package com.example.wishlist.controller;

import com.example.wishlist.dto.WishDTO;
import com.example.wishlist.model.Wish;
import com.example.wishlist.model.Wishlist;
import com.example.wishlist.service.WishService;
import com.example.wishlist.service.WishlistService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
public class WishController {

    private final WishlistService wishlistService;
    private final WishService wishService;

    public WishController(WishlistService wishlistService, WishService wishService) {
        this.wishService = wishService;
        this.wishlistService = wishlistService;
    }

    @GetMapping("/{id}/wishes")
    public String getAllWishes(@PathVariable Long id,
                               Model model) {
        Wishlist wishlist = wishlistService.getWishlistById(id);
        List<Wish> wishes = wishlist.getWishes();

        WishDTO wishDTO = new WishDTO();

        model.addAttribute("wlId", id);
        model.addAttribute("wishes", wishes);
        model.addAttribute("wishDTO", wishDTO);
        return "wishes";
    }

    @PostMapping("{id}/wishes/create")
    public String createWish(@PathVariable Long id,
                             @Valid @ModelAttribute("wishDTO") WishDTO wishDTO,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "wishes";
        }

        Wishlist wishlist = wishlistService.getWishlistById(id);
        Wish wish = new Wish();
        wish.setDescription(wishDTO.getDescription());
        wishlist.addWish(wish);

        wishService.saveWish(wish);
        wishlistService.saveWishlist(wishlist);

        return "redirect:/" + id + "/wishes";
    }

    @GetMapping("{wlId}/wishes/{wId}/delete")
    public String deleteWish(@PathVariable(name = "wlId") Long wishlistId,
                             @PathVariable(name = "wId") Long wishId) {

        Wishlist wishlist = wishlistService.getWishlistById(wishlistId);
        wishlist.removeWish(wishId);

        wishlistService.saveWishlist(wishlist);
        wishService.deleteWish(wishId);

        return "redirect:/" + wishlistId + "/wishes";
    }

    @GetMapping("{wlId}/wishes/{wId}/edit")
    public String getEditWishPage(@PathVariable(name = "wlId") Long wishlistId,
                                  @PathVariable(name = "wId") Long wishId,
                                  Model model) {

        Wish wish = wishService.getWishById(wishId);
        WishDTO wishDTO = new WishDTO();
        wishDTO.setId(wishId);

        model.addAttribute("wishDTO", wishDTO);
        model.addAttribute("wlId", wishlistId);
        model.addAttribute("oldDescription", wish.getDescription());

        return "edit-wish";
    }

    @PostMapping("{wlId}/wishes/edit")
    public String editWish(@PathVariable(name = "wlId") Long wishlistId,
                           @Valid @ModelAttribute("wishDTO")WishDTO wishDTO,
                           BindingResult result) {

        if (result.hasErrors()) {
            return "edit-wish";
        }

        Wish wish = wishService.getWishById(wishDTO.getId());
        wish.setDescription(wishDTO.getDescription());
        wishService.saveWish(wish);

        return "redirect:/" + wishlistId + "/wishes";
    }
}
