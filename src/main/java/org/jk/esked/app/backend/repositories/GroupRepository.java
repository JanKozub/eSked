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
    List<Integer> getAllGroupCodes();

    @Query("select g.leader.id from Group g where g.group_code = :groupCode")
    UUID getLeaderIdByGroupCode(int groupCode);

    @Query("select g from Group g where g.group_code = :groupCode")
    Group getGroupByGroupCode(int groupCode);

    @Transactional
    @Modifying
    @Query("delete from Group g where g.group_code = :groupCode")
    void deleteGroupByGroupCode(int groupCode);

    @Transactional
    @Modifying
    @Query("UPDATE Group g SET g.is_accepted = :state WHERE g.group_code = :groupCode")
    void changeGroupAcceptedByGroupCode(int groupCode, boolean state);
}
