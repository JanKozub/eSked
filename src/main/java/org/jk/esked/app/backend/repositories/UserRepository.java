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
    List<String> getAllUsersUsernames();

    @Query("SELECT u.email FROM User u")
    List<String> getAllUsersEmails();

    @Query("SELECT u.username FROM User u WHERE u.id = :id")
    String getUserUsernameById(UUID id);

    @Query("SELECT u.email FROM User u WHERE u.id = :id")
    String getUserEmailById(UUID id);

    @Query("SELECT u.dark_theme FROM User u WHERE u.id = :id")
    boolean getUserDarkThemeById(UUID id);

    @Query("select u.schedule_hours from User u where u.id = :id")
    boolean isScheduleHourEnabledById(UUID id);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(String username);

    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    UUID getUserIdByUsername(String username);

    @Query("SELECT u.group_code FROM User u WHERE u.id = :id")
    int getGroupCodeByUserId(UUID id);

    @Query("SELECT u.events_sync FROM User u WHERE u.id = :id")
    boolean isEventSynByUserId(UUID id);

    @Query("SELECT u.table_sync FROM User u WHERE u.id = :id")
    boolean isTableSynByUserId(UUID id);

    @Query("SELECT u.user_type FROM User u WHERE u.id = :id")
    UserType getUserTypeByUserId(UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void changeUserUsernameById(UUID id, String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void changeUserPasswordById(UUID id, String password);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void changeUserEmailById(UUID id, String password);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.verified = :verified WHERE u.id = :id")
    void changeUserVerifiedById(UUID id, boolean verified);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.group_code = :groupCode WHERE u.id = :id")
    void changeGroupCodeByUserId(UUID id, int groupCode);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.schedule_hours = :state WHERE u.id = :id")
    void changeHoursByUserId(UUID id, boolean state);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.dark_theme = :state WHERE u.id = :id")
    void changeThemeByUserId(UUID id, boolean state);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.events_sync = :state WHERE u.id = :id")
    void changeEventsSynByUserId(UUID id, boolean state);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.table_sync = :state WHERE u.id = :id")
    void changeTableSynByUserId(UUID id, boolean state);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.user_type = :userType WHERE u.id = :id")
    void changeUserTypeById(UUID id, UserType userType);


}
