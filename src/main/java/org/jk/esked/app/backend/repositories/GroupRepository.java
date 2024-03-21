package org.jk.esked.app.backend.repositories;

import jakarta.transaction.Transactional;
import org.jk.esked.app.backend.model.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
    @Query("select g.group_code from Group g")
    List<Integer> findAllGroupCodes();

    @Query("select g.leader.id from Group g where g.group_code = :groupCode")
    UUID findLeaderIdByGroupCode(int groupCode);

    @Query("select g from Group g where g.group_code = :groupCode")
    Group findByGroupCode(int groupCode);

    @Query("select g from Group g where g.is_accepted = :accepted")
    List<Group> findAllGroupsByAccepted(boolean accepted);

    @Transactional
    @Modifying
    @Query("delete from Group g where g.group_code = :groupCode")
    void deleteByGroupCode(int groupCode);

    @Transactional
    @Modifying
    @Query("UPDATE Group g SET g.is_accepted = :state WHERE g.group_code = :groupCode")
    void changeAcceptedByGroupCode(int groupCode, boolean state);
}
