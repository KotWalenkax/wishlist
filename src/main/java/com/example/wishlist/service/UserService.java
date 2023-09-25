package com.example.wishlist.service;

import com.example.wishlist.model.User;
import com.example.wishlist.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserLoggedInfoService userLoggedInfoService;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, UserLoggedInfoService userLoggedInfoService) {
        this.userRepository = userRepository;
        this.userLoggedInfoService = userLoggedInfoService;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean isUserExists(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    public void editUserProfile(User user) {
        userLoggedInfoService.editUsername(user.getUsername());
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User getUserByUsername() {
        return userRepository.findUserByUsername(userLoggedInfoService.getUsername());
    }

}
