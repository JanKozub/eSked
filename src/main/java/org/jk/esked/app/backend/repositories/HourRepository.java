package org.jk.esked.app.backend.repositories;

import jakarta.transaction.Transactional;
import org.jk.esked.app.backend.model.entities.Hour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface HourRepository extends JpaRepository<Hour, UUID> {

    @Query("select h from Hour h where h.user.id = :id")
    List<Hour> findHoursByUserId(UUID id);

    @Query("select distinct h from Hour h where h.user.id = :id and h.hour = :hour")
    Hour getHoursByUserIdAndHour(UUID id, int hour);

    @Transactional
    @Modifying
    @Query("delete from Hour h where h.user.id = :id")
    void deleteAllHoursByUserId(UUID id);

    @Transactional
    @Modifying
    @Query("delete from Hour h where h.user.id = :id and h.hour = :hour")
    void deleteHourByUserIdAndHour(UUID id, int hour);
}
