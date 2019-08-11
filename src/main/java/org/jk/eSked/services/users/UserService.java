package org.jk.eSked.services.users;

import org.jk.eSked.model.User;

import java.util.Collection;
import java.util.UUID;

public interface UserService {

    void addUser(User user);

    Collection<User> getUsers();

    Collection<String> getUsernames();

    void changeUsername(UUID userId, String newUsername);

    void changePassword(UUID userId, String newPassword);

    void changeEmail(UUID userId, String newEmail);

    void deleteUser(UUID userId);

    boolean getDarkTheme(UUID userId);

    void setDarkTheme(UUID userId, boolean state);

    void setLastLogged(UUID userId, long time);

    int getGroupCode(UUID userId);

    void setGroupCode(UUID userId, int groupCode);

    void setScheduleHours(UUID userId, boolean state);

    boolean getScheduleHours(UUID userId);

    boolean isSynWGroup(UUID userId);

    void setSynWGroup(UUID userId, boolean state);
}
