package org.jk.eSked.backend.service.user;

import org.jk.eSked.backend.dao.UsersDao;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.ThemeType;
import org.jk.eSked.backend.repositories.UserDB;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDB {

    private final UsersDao usersDao;

    public UserService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    //USER

    @Override
    public Collection<User> getUsers() {
        return usersDao.getUsers();
    }

    @Override
    public Collection<UserDetails> getUserDetails() {
        Collection<User> users = getUsers();
        List<UserDetails> userDetails = new ArrayList<>();

        users.forEach(user -> {
            UserDetails newUser =
                    org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                            .password("{noop}" + user.getPassword())
                            .roles("USER")
                            .build();
            userDetails.add(newUser);
        });
        return userDetails;
    }

    @Override
    public User getUser(UUID userId) {
        return usersDao.getUser(userId);
    }

    @Override
    public void addUser(User user) {
        usersDao.persistUser(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        usersDao.deleteUser(userId);
    }

    //USERNAME

    @Override
    public Collection<String> getUsernames() {
        return usersDao.getUsernames();
    }

    @Override
    public String getUsername(UUID userId) {
        return usersDao.getUsername(userId);
    }

    @Override
    public void setUsername(UUID userId, String newUsername) {
        usersDao.changeUsername(userId, newUsername);
    }

    //PASSWORD

    @Override
    public void changePassword(UUID userId, String newPassword) {
        usersDao.changePassword(userId, newPassword);
    }


    //EMAIL

    @Override
    public Collection<String> getEmails() {
        return usersDao.getEmails();
    }

    @Override
    public String getEmail(UUID userId) {
        return usersDao.getEmail(userId);
    }

    @Override
    public void setEmail(UUID userId, String newEmail) {
        usersDao.changeEmail(userId, newEmail);
    }

    //THEME

    @Override
    public ThemeType getTheme(UUID userId) {
        if (usersDao.getDarkTheme(userId)) return ThemeType.DARK;
        else return ThemeType.WHITE;
    }

    @Override
    public void setTheme(UUID userId, ThemeType themeType) {
        if (themeType == ThemeType.DARK) usersDao.setDarkTheme(userId, true);
        else usersDao.setDarkTheme(userId, false);
    }

    //GROUPS

    @Override
    public int getGroupCode(UUID userId) {
        return usersDao.getGroupCode(userId);
    }

    @Override
    public void setGroupCode(UUID userId, int groupCode) {
        usersDao.setGroupCode(userId, groupCode);
    }

    //SCHEDULE HOURS

    @Override
    public boolean getScheduleHours(UUID userId) {
        return usersDao.getScheduleHours(userId);
    }

    @Override
    public void setScheduleHours(UUID userId, boolean state) {
        usersDao.setScheduleHours(userId, state);
    }

    //SYNCHRONIZATION

    @Override
    public boolean isEventsSyn(UUID userId) {
        return usersDao.isEventsSyn(userId);
    }

    @Override
    public boolean isTableSyn(UUID userId) {
        return usersDao.isTableSyn(userId);
    }

    @Override
    public void setEventsSyn(UUID userId, boolean state) {
        usersDao.setEventsSyn(userId, state);
    }

    @Override
    public void setTableSyn(UUID userId, boolean state) {
        usersDao.setTableSyn(userId, state);
    }

    @Override
    public void setVerified(UUID userId, boolean newState) {
        usersDao.setVerified(userId, newState);
    }

    //TIME

    @Override
    public void setLastLogged(UUID userId, long time) {
        usersDao.setLastLogged(userId, time);
    }

    //OTHER GETTERS

    @Override
    public String getEmailByUsername(String username) {
        return usersDao.getEmailFromUsername(username);
    }

    @Override
    public UUID getIdByUsername(String username) {
        return usersDao.getIdFromUsername(username);
    }

    @Override
    public User getUserByUsername(String username) {
        return usersDao.getUserByUsername(username);
    }
}