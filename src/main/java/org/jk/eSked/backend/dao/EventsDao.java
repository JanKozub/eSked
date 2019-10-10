package org.jk.eSked.backend.dao;

import org.apache.ibatis.annotations.*;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.types.EventType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventsDao {
    @Select("SELECT * FROM Events WHERE timestamp >= #{startOfWeek} AND timestamp < #{startOfWeek} + CAST(7*24*3600 AS LONG) * 1000 AND userId = #{userId}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "eventId", javaType = UUID.class),
            @Arg(column = "type", javaType = EventType.class),
            @Arg(column = "topic", javaType = String.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "checkedFlag", javaType = boolean.class),
            @Arg(column = "timestamp", javaType = long.class),
            @Arg(column = "createdTimestamp", javaType = long.class)
    })
    List<Event> getEventsForWeek(long startOfWeek, UUID userId);

    @Select("SELECT * FROM Events WHERE userId = #{userId}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "eventId", javaType = UUID.class),
            @Arg(column = "type", javaType = EventType.class),
            @Arg(column = "topic", javaType = String.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "checkedFlag", javaType = boolean.class),
            @Arg(column = "timestamp", javaType = long.class),
            @Arg(column = "createdTimestamp", javaType = long.class)
    })
    List<Event> getEvents(UUID userId);

    @Select("SELECT * FROM Events WHERE eventId = #{eventId}")
    @ConstructorArgs({
            @Arg(column = "eventId", javaType = UUID.class),
    })
    List<UUID> getUUIDs(UUID eventId);

    @Insert("INSERT INTO Events(userId, eventId, type, topic, hour, checkedFlag, timestamp, createdTimestamp) " +
            "VALUES(#{userId}, #{eventId}, #{type}, #{topic}, #{hour}, #{checkedFlag}, #{timestamp}, #{createdTimestamp})")
    void persistEvent(Event event);

    @Delete("DELETE FROM Events WHERE userId = #{userId} AND eventId = #{eventId}")
    void deleteEvent(UUID userId, UUID eventId);

    @Update("UPDATE Events SET checkedFlag = #{newState} WHERE eventId = #{eventId} AND userId = #{userId}")
    void setCheckedFlag(UUID eventId, UUID userId, boolean newState);
}
