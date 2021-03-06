package org.jk.eSked.backend.dao;

import org.apache.ibatis.annotations.*;
import org.jk.eSked.backend.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsersDao {

    @Insert("INSERT INTO Users(id, Username, Password, darkTheme, scheduleHours, email, groupCode, eventsSyn, tableSyn, createdDate, lastLoggedDate, verified) " +
            "VALUES(#{id}, #{username}, #{password}, #{darkTheme}, #{scheduleHours}, #{email}, #{groupCode}, #{eventsSyn}, #{tableSyn}, #{createdDate}, #{lastLoggedDate}, #{verified})")
    void persistUser(User user);

    @Select("SELECT * FROM Users")
    @ConstructorArgs({
            @Arg(column = "id", javaType = UUID.class),
            @Arg(column = "Username", javaType = String.class),
            @Arg(column = "password", javaType = String.class),
            @Arg(column = "darkTheme", javaType = boolean.class),
            @Arg(column = "ScheduleHours", javaType = boolean.class),
            @Arg(column = "email", javaType = String.class),
            @Arg(column = "groupCode", javaType = int.class),
            @Arg(column = "eventsSyn", javaType = boolean.class),
            @Arg(column = "tableSyn", javaType = boolean.class),
            @Arg(column = "createdDate", javaType = long.class),
            @Arg(column = "lastLoggedDate", javaType = long.class),
            @Arg(column = "verified", javaType = boolean.class)
    })
    List<User> getUsers();

    @Select("SELECT * FROM Users WHERE id = #{userId}")
    @ConstructorArgs({
            @Arg(column = "id", javaType = UUID.class),
            @Arg(column = "Username", javaType = String.class),
            @Arg(column = "password", javaType = String.class),
            @Arg(column = "darkTheme", javaType = boolean.class),
            @Arg(column = "ScheduleHours", javaType = boolean.class),
            @Arg(column = "email", javaType = String.class),
            @Arg(column = "groupCode", javaType = int.class),
            @Arg(column = "eventsSyn", javaType = boolean.class),
            @Arg(column = "tableSyn", javaType = boolean.class),
            @Arg(column = "createdDate", javaType = long.class),
            @Arg(column = "lastLoggedDate", javaType = long.class),
            @Arg(column = "verified", javaType = boolean.class)
    })
    User getUser(UUID userId);

    @Select("SELECT * FROM Users WHERE username = #{username}")
    @ConstructorArgs({
            @Arg(column = "id", javaType = UUID.class),
            @Arg(column = "Username", javaType = String.class),
            @Arg(column = "password", javaType = String.class),
            @Arg(column = "darkTheme", javaType = boolean.class),
            @Arg(column = "ScheduleHours", javaType = boolean.class),
            @Arg(column = "email", javaType = String.class),
            @Arg(column = "groupCode", javaType = int.class),
            @Arg(column = "eventsSyn", javaType = boolean.class),
            @Arg(column = "tableSyn", javaType = boolean.class),
            @Arg(column = "createdDate", javaType = long.class),
            @Arg(column = "lastLoggedDate", javaType = long.class),
            @Arg(column = "verified", javaType = boolean.class)
    })
    User getUserByUsername(String username);

    @Select("SELECT * FROM Users")
    @ConstructorArgs({
            @Arg(column = "Username", javaType = String.class)
    })
    List<String> getUsernames();

    @Select("SELECT * FROM Users WHERE id = #{userId}")
    @ConstructorArgs({
            @Arg(column = "Username", javaType = String.class)
    })
    String getUsername(UUID userId);

    @Select("SELECT * FROM Users")
    @ConstructorArgs({
            @Arg(column = "email", javaType = String.class)
    })
    List<String> getEmails();

    @Select("SELECT * FROM Users WHERE id = #{userId}")
    @ConstructorArgs({
            @Arg(column = "email", javaType = String.class)
    })
    String getEmail(UUID userId);

    @Update("UPDATE Users SET USERNAME = #{newUsername} WHERE id = #{userId}")
    void changeUsername(UUID userId, String newUsername);

    @Update("UPDATE Users SET PASSWORD = #{newPassword} WHERE id = #{userId}")
    void changePassword(UUID userId, String newPassword);

    @Update("UPDATE Users SET EMAIL = #{newEmail} WHERE id = #{userId}")
    void changeEmail(UUID userId, String newEmail);

    @Delete("DELETE FROM USERS WHERE id = #{userId}")
    void deleteUser(UUID userId);

    @Select("SELECT * FROM USERS WHERE id = #{userId}")
    @ConstructorArgs({
            @Arg(column = "darkTheme", javaType = boolean.class)
    })
    boolean getDarkTheme(UUID userId);

    @Update("UPDATE Users SET darkTheme = #{state} WHERE id = #{userId}")
    void setDarkTheme(UUID userId, boolean state);

    @Update("UPDATE Users SET lastLoggedDate = #{time} WHERE id = #{userId}")
    void setLastLogged(UUID userId, long time);

    @Select("SELECT * FROM USERS WHERE id = #{userId}")
    @ConstructorArgs({
            @Arg(column = "groupCode", javaType = int.class)
    })
    int getGroupCode(UUID userId);

    @Update("UPDATE Users SET groupCode = #{groupCode} WHERE id = #{userId}")
    void setGroupCode(UUID userId, int groupCode);

    @Update("UPDATE Users SET scheduleHours = #{scheduleHours} WHERE id = #{userId}")
    void setScheduleHours(UUID userId, boolean scheduleHours);

    @Select("SELECT * FROM USERS WHERE id = #{userId}")
    @ConstructorArgs({
            @Arg(column = "scheduleHours", javaType = boolean.class)
    })
    boolean getScheduleHours(UUID userId);

    @Select("SELECT * FROM USERS WHERE id = #{userId}")
    @ConstructorArgs({
            @Arg(column = "eventsSyn", javaType = boolean.class)
    })
    boolean isEventsSyn(UUID userId);

    @Select("SELECT * FROM USERS WHERE id = #{userId}")
    @ConstructorArgs({
            @Arg(column = "tableSyn", javaType = boolean.class)
    })
    boolean isTableSyn(UUID userId);

    @Update("UPDATE USERS SET eventsSyn = #{state} WHERE id = #{userId}")
    void setEventsSyn(UUID userId, boolean state);

    @Update("UPDATE USERS SET tableSyn = #{state} WHERE id = #{userId}")
    void setTableSyn(UUID userId, boolean state);

    @Update("UPDATE Users SET verified = #{state} WHERE id = #{userId}")
    void setVerified(UUID userId, boolean state);

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    @ConstructorArgs({
            @Arg(column = "id", javaType = UUID.class)
    })
    UUID getIdFromUsername(String username);

    @Select("SELECT * FROM USERS WHERE email = #{email}")
    @ConstructorArgs({
            @Arg(column = "username", javaType = String.class)
    })
    String getUsernameByEmail(String email);
}
