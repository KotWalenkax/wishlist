package com.example.wishlist.controller;

import com.example.wishlist.dto.UserDTO;
import com.example.wishlist.model.User;
import com.example.wishlist.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
@RequestMapping("/")
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    public UserController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/profile";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {

        model.addAttribute("userDTO", new UserDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String createNewUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                                BindingResult result) {

        if (result.hasErrors()) {
            return "registration";
        }

        User user = new User();
        user.setUsername(userDTO.getUsername().toLowerCase(Locale.ROOT));
        user.setPassword(encoder.encode(userDTO.getPassword()));
        userService.saveUser(user);

        return "login";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {

        User user = userService.getUserByUsername();
        model.addAttribute("user", user);
        return "user-profile";
    }

    @GetMapping("/profile/edit")
    public String getEditProfilePage(Model model) {

        model.addAttribute("userDTO", new UserDTO());

        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@Valid @ModelAttribute(name = "userDTO") UserDTO userDTO,
                              BindingResult result) {

        if (result.hasErrors()) {
            return "edit-profile";
        }

        User user = userService.getUserByUsername();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        userService.editUserProfile(user);

        return "redirect:/profile";
    }

}
