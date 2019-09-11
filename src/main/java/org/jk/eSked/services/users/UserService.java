package org.jk.eSked.services.users;

import org.jk.eSked.model.User;

import java.util.Collection;
import java.util.UUID;

public interface UserService {

    void addUser(User user);

    Collection<User> getUsers();

    Collection<String> getUsernames();

    Collection<String> getEmails();

    String getUsername(UUID userId);

    void changeUsername(UUID userId, String newUsername);

    void changePassword(UUID userId, String newPassword);

    String getEmail(UUID userId);

    void changeEmail(UUID userId, String newEmail);

    void deleteUser(UUID userId);

    boolean getDarkTheme(UUID userId);

    void setDarkTheme(UUID userId, boolean state);

    void setLastLogged(UUID userId, long time);

    int getGroupCode(UUID userId);

    void setGroupCode(UUID userId, int groupCode);

    void setScheduleHours(UUID userId, boolean state);

    boolean getScheduleHours(UUID userId);

    boolean isEventsSyn(UUID userId);

    boolean isTableSyn(UUID userId);

    void setEventsSyn(UUID userId, boolean state);

    void setTableSyn(UUID userId, boolean state);

    String getEmailFromUsername(String username);

    UUID getIdFromUsername(String username);
}
