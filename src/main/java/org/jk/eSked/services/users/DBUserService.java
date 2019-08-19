package org.jk.eSked.services.users;

import org.jk.eSked.dao.UsersDao;
import org.jk.eSked.model.User;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Collection;
import java.util.UUID;

@SuppressWarnings("unused")
@Service
public class DBUserService implements UserService {

    private final UsersDao usersDao;

    public DBUserService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public Collection<User> getUsers() {
        return usersDao.getUsers();
    }

    @Override
    public Collection<String> getUsernames() {
        return usersDao.getUsernames();
    }

    @Override
    public Collection<String> getEmails() {
        return usersDao.getEmails();
    }

    @Override
    public void addUser(User user) {
        usersDao.persistUser(user.getId(), user.getUsername(), user.getPassword(), user.isDarkTheme(), user.isScheduleHours(),
                user.getEmail(), user.getGroupCode(), user.isSynWGroup(), user.getCreatedDate().toInstant(ZoneOffset.UTC).toEpochMilli(),
                user.getLastLoggedDate().toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    @Override
    public void deleteUser(UUID userId) {
        usersDao.deleteUser(userId);
    }

    @Override
    public String getUserName(UUID userId) {
        return usersDao.getUsername(userId);
    }

    @Override
    public void changeUsername(UUID userId, String newUsername) {
        usersDao.changeUsername(userId, newUsername);
    }

    @Override
    public void changePassword(UUID userId, String newPassword) {
        usersDao.changePassword(userId, newPassword);
    }

    @Override
    public String getEmail(UUID userId) {
        return usersDao.getEmail(userId);
    }

    @Override
    public void changeEmail(UUID userId, String newEmail) {
        usersDao.changeEmail(userId, newEmail);
    }

    @Override
    public boolean getDarkTheme(UUID userId) {
        return usersDao.getDarkTheme(userId);
    }

    @Override
    public void setDarkTheme(UUID userId, boolean state) {
        usersDao.setDarkTheme(userId, state);
    }

    @Override
    public void setLastLogged(UUID userId, long time) {
        usersDao.setLastLogged(userId, time);
    }

    @Override
    public int getGroupCode(UUID userId) {
        return usersDao.getGroupCode(userId);
    }

    @Override
    public void setGroupCode(UUID userId, int groupCode) {
        usersDao.setGroupCode(userId, groupCode);
    }

    @Override
    public boolean getScheduleHours(UUID userId) {
        return usersDao.getScheduleHours(userId);
    }

    @Override
    public void setScheduleHours(UUID userId, boolean state) {
        usersDao.setScheduleHours(userId, state);
    }

    @Override
    public boolean isSynWGroup(UUID userId) {
        return usersDao.isSynWGroup(userId);
    }

    @Override
    public void setSynWGroup(UUID userId, boolean state) {
        usersDao.setSynWGroup(userId, state);
    }
}