package com.example.wishlist.service;

import com.example.wishlist.model.User;
import com.example.wishlist.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserLoggedInfoService userLoggedInfoService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void isUserExistsSuccessfulTest() {
        User user = createTestUser();

        given(userRepository.findUserByUsername(user.getUsername()))
                .willReturn(user);

        Assertions.assertTrue(userService.isUserExists(user.getUsername()));
    }

    @Test
    public void isUserExistsFailTest() {
        User user = createTestUser();

        given(userRepository.findUserByUsername(user.getUsername()))
                .willReturn(null);

        Assertions.assertFalse(userService.isUserExists(user.getUsername()));
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("usertest");
        user.setPassword("password");

        return user;
    }
}
