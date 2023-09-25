package com.example.wishlist.service;

import com.example.wishlist.model.User;
import com.example.wishlist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Locale;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntegrationTest {

    @MockBean
    UserLoggedInfoService userLoggedInfoService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @AfterEach
    public void clearUser() {
        userRepository.deleteAll();
    }

    @Test
    public void saveUserTest() {
        User user = initUserAndAddToDB();

        userService.saveUser(user);

        User userTest = userRepository.findUserByUsername(user.getUsername());

        Assertions.assertEquals(user, userTest);
    }

    @Test
    public void isUserExistsReturnedTrueTest() {
        initUserAndAddToDB();

        Assertions.assertTrue(userService.isUserExists("usertest"));
    }

    @Test
    public void isUserExistsWithReturnedFalseTest() {
        Assertions.assertFalse(userService.isUserExists("usertest"));
    }

    @Test
    public void createTestToUpdateData() {
        User user = initUserAndAddToDB();

        String oldName = user.getUsername();
        String newName = "newname";

        user.setUsername(newName);

        userService.saveUser(user);

        User testUser = userRepository.findUserByUsername(newName);

        Assertions.assertNotNull(testUser);
        Assertions.assertEquals(1, userRepository.count());
        Assertions.assertNotEquals(oldName, testUser.getUsername());
    }

    public User initUserAndAddToDB() {
        User user = new User();
        user.setUsername("usertest");
        user.setPassword("password");

        userRepository.save(user);
        return user;
    }

    public User initUser() {
        User user = new User();
        user.setUsername("UserTest");
        user.setPassword("password");

        return user;
    }
}
