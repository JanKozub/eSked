package org.jk.esked.app.backend.repositories;

import jakarta.transaction.Transactional;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.types.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();

    @Query("SELECT u.email FROM User u")
    List<String> findAllEmails();

    @Query("SELECT u.username FROM User u WHERE u.id = :id")
    String findUsernameById(UUID id);

    @Query("SELECT u.email FROM User u WHERE u.id = :id")
    String findEmailById(UUID id);

    @Query("SELECT u.dark_theme FROM User u WHERE u.id = :id")
    boolean findUserDarkThemeById(UUID id);

    @Query("select u.schedule_hours from User u where u.id = :id")
    boolean isScheduleHourEnabledById(UUID id);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findUserByUsername(String username);

    @Query("SELECT u.group_code FROM User u WHERE u.id = :id")
    int findGroupCodeById(UUID id);

    @Query("SELECT u.events_sync FROM User u WHERE u.id = :id")
    boolean isEventSynById(UUID id);

    @Query("SELECT u.table_sync FROM User u WHERE u.id = :id")
    boolean isTableSynById(UUID id);

    @Query("SELECT u.user_type FROM User u WHERE u.id = :id")
    UserType findUserTypeById(UUID id);

    @Query("select u from User u where u.email = :email")
    User findUserByEmail(String email);

    @Query("select u from User u where u.group_code = :groupCode")
    List<User> findAllUsersByGroupCode(int groupCode);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void changeUsernameById(UUID id, String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void changePasswordById(UUID id, String password);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void changeEmailById(UUID id, String password);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.verified = :verified WHERE u.id = :id")
    void changeUserVerifiedById(UUID id, boolean verified);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.group_code = :groupCode WHERE u.id = :id")
    void changeGroupCodeById(UUID id, int groupCode);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.schedule_hours = :state WHERE u.id = :id")
    void changeHoursById(UUID id, boolean state);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.dark_theme = :state WHERE u.id = :id")
    void changeThemeById(UUID id, boolean state);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.events_sync = :state WHERE u.id = :id")
    void changeEventsSynById(UUID id, boolean state);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.table_sync = :state WHERE u.id = :id")
    void changeTableSynById(UUID id, boolean state);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.user_type = :userType WHERE u.id = :id")
    void changeUserTypeById(UUID id, UserType userType);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.last_logged_timestamp = :now WHERE u.id = :id")
    void changeLastLoggedInById(UUID id, long now);
}
