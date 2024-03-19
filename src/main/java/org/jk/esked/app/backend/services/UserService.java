package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.User;
import org.jk.esked.app.backend.model.types.ThemeType;
import org.jk.esked.app.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<String> getAllUserUsernames() {
        return userRepository.getAllUsersUsernames();
    }

    public String getUsernameByUserId(UUID id) {
        return userRepository.getUserUsernameById(id);
    }

    public void changeUsernameByUserId(UUID id, String username) {
        userRepository.changeUserUsernameById(id, username);
    }

    public void changePasswordByUserId(UUID userId, String newPassword) {
        userRepository.changeUserPasswordById(userId, newPassword);
    }

    public List<String> getAllUserEmails() {
        return userRepository.getAllUsersEmails();
    }

    public String getEmailByUserId(UUID userId) {
        return userRepository.getUserEmailById(userId);
    }

    public void changeEmailByUserId(UUID userId, String newEmail) {
        userRepository.changeUserEmailById(userId, newEmail);
    }

    public ThemeType getThemeByUserId(UUID userId) {
        return (userRepository.getUserDarkThemeById(userId)) ? ThemeType.DARK : ThemeType.WHITE;
    }

    public void changeThemeByUserId(UUID userId, ThemeType themeType) {
        userRepository.changeThemeByUserId(userId, themeType == ThemeType.DARK);
    }

    public int getGroupCodeByUserId(UUID userId) {
        return userRepository.getGroupCodeByUserId(userId);
    }

    public void changeGroupCodeByUserId(UUID userId, int groupCode) {
        userRepository.changeGroupCodeByUserId(userId, groupCode);
    }

    public boolean isHourEnabled(UUID userId) {
        return userRepository.isScheduleHourEnabledById(userId);
    }

    public void changeHourByUserId(UUID userId, boolean state) {
        userRepository.changeHoursByUserId(userId, state);
    }

    public boolean isEventsSynByUserId(UUID userId) {
        return userRepository.isEventSynByUserId(userId);
    }

    public boolean isTableSyn(UUID userId) {
        return userRepository.isTableSynByUserId(userId);
    }

    public void changeEventsSynByUserId(UUID userId, boolean state) {
        userRepository.changeEventsSynByUserId(userId, state);
    }

    public void changeTableSynByUserId(UUID userId, boolean state) {
        userRepository.changeTableSynByUserId(userId, state);
    }

    public void changeVerifiedByUserId(UUID userId, boolean newState) {
        userRepository.changeUserVerifiedById(userId, newState);
    }

    public UUID getIdByUsername(String username) {
        return userRepository.getUserIdByUsername(username);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
}