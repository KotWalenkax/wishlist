package com.example.wishlist.controller;

import com.example.wishlist.dto.UserDTO;
import com.example.wishlist.model.User;
import com.example.wishlist.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class)
@WithMockUser
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder encoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getRegistrationPageTest() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userDTO", notNullValue()))
                .andExpect(view().name("registration"));
    }

    @Test
    public void createNewUserSuccessfulTest() throws Exception {
        User user = new User();

        given(userService.isUserExists(anyString())).willReturn(false);
        given(encoder.encode(anyString())).willReturn("encodedPassword");

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .sessionAttr("user", user)
                        .param("username", "userTest")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void createNewUserWithNullUsernameFieldTest() throws Exception {
        User user = new User();

        given(userService.isUserExists(anyString())).willReturn(false);

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .sessionAttr("user", user)
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void createNewUserWithNullPasswordFieldTest() throws Exception {
        User user = new User();

        given(userService.isUserExists(anyString())).willReturn(false);

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .sessionAttr("user", user)
                        .param("user", "userTest"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));

        verify(userService, never()).isUserExists(anyString());
    }

    @Test
    public void createNewUserWithExistingUserTest() throws Exception {
        User user = new User();

        given(userService.isUserExists(anyString())).willReturn(true);

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .sessionAttr("user", user)
                        .param("username", "userTest")
                        .param("password", "password"))
                .andExpect(status().isOk());

        verify(userService, never()).saveUser(ArgumentMatchers.any());
        verify(encoder, never()).encode(anyString());
    }

    @Test
    public void getProfilePageTest() throws Exception {
        User user = new User();
        user.setUsername("usertest");
        user.setPassword("password");

        given(userService.getUserByUsername()).willReturn(user);

        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(view().name("user-profile"));
    }

    @Test
    public void getEditProfilePageTest() throws Exception {
        mockMvc.perform(get("/profile/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userDTO", notNullValue()))
                .andExpect(view().name("edit-profile"));
    }

    @Test
    public void editProfileSuccessfulTest() throws Exception {
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");

        given(userService.getUserByUsername()).willReturn(user);

        mockMvc.perform(post("/profile/edit")
                    .with(csrf())
                    .sessionAttr("userDTO", userDTO)
                    .param("username", user.getUsername())
                    .param("password", user.getPassword()))
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    public void editProfileNullUsernameTest() throws Exception {
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");

        mockMvc.perform(post("/profile/edit")
                        .with(csrf())
                        .sessionAttr("userDTO", userDTO)
                        .param("password", user.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-profile"));

        verify(userService, never()).getUserByUsername();
    }

    @Test
    public void editProfileNullPasswordTest() throws Exception {
        UserDTO userDTO = new UserDTO();

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");

        mockMvc.perform(post("/profile/edit")
                        .with(csrf())
                        .sessionAttr("userDTO", userDTO)
                        .param("username", user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-profile"));

        verify(userService, never()).getUserByUsername();
    }
}
