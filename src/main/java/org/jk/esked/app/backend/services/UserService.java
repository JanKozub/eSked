package org.jk.esked.app.backend.services;

import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.types.ThemeType;
import org.jk.esked.app.backend.model.types.UserType;
import org.jk.esked.app.backend.repositories.UserRepository;
import org.jk.esked.app.backend.services.utilities.TimeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveUser(String username, String password, String email) {
        userRepository.save(new User(username, password, email));
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    public User findById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<String> findAllUsernames() {
        return userRepository.findAllUsernames();
    }

    public boolean isUsernameRegistered(String username) {
        return userRepository.findAllUsernames().contains(username);
    }

    public String findUsernameById(UUID id) {
        return userRepository.findUsernameById(id);
    }

    public void changeUsernameById(UUID id, String username) {
        userRepository.changeUsernameById(id, username);
    }

    public void changePasswordById(UUID id, String newPassword) {
        userRepository.changePasswordById(id, newPassword);
    }

    public List<String> findAllRegisteredEmails() {
        return userRepository.findAllEmails();
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.findAllEmails().contains(email);
    }

    public String findEmailById(UUID userId) {
        return userRepository.findEmailById(userId);
    }

    public void changeEmailById(UUID userId, String newEmail) {
        userRepository.changeEmailById(userId, newEmail);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public ThemeType findThemeById(UUID id) {
        return (userRepository.findUserDarkThemeById(id)) ? ThemeType.DARK : ThemeType.WHITE;
    }

    public void changeThemeById(UUID id, ThemeType themeType) {
        userRepository.changeThemeById(id, themeType == ThemeType.DARK);
    }

    public int findGroupCodeById(UUID id) {
        return userRepository.findGroupCodeById(id);
    }

    public List<User> findAllUsersByGroupCode(int groupCode) {
        return userRepository.findAllUsersByGroupCode(groupCode);
    }

    public void changeGroupCodeById(UUID id, int groupCode) {
        userRepository.changeGroupCodeById(id, groupCode);
    }

    public boolean isHourEnabled(UUID id) {
        return userRepository.isScheduleHourEnabledById(id);
    }

    public void changeHourById(UUID id, boolean state) {
        userRepository.changeHoursById(id, state);
    }

    public boolean isEventsSynById(UUID id) {
        return userRepository.isEventSynById(id);
    }

    public boolean isTableSynById(UUID id) {
        return userRepository.isTableSynById(id);
    }

    public void changeEventsSynById(UUID id, boolean state) {
        userRepository.changeEventsSynById(id, state);
    }

    public void changeTableSynById(UUID id, boolean state) {
        userRepository.changeTableSynById(id, state);
    }

    public void changeVerifiedById(UUID id, boolean newState) {
        userRepository.changeUserVerifiedById(id, newState);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public UserType findUserTypeById(UUID id) {
        return userRepository.findUserTypeById(id);
    }

    public void changeUserTypeById(UUID id, UserType userType) {
        userRepository.changeUserTypeById(id, userType);
    }

    public void changeLastLoggedInById(UUID id) {
        userRepository.changeLastLoggedInById(id, TimeService.now());
    }
}