package org.jk.eSked.backend.dao;

import org.apache.ibatis.annotations.*;
import org.jk.eSked.backend.model.Event;
import org.jk.eSked.backend.model.types.EventType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventsDao {
    @Select("SELECT * FROM Events WHERE timestamp >= #{startOfWeek} AND timestamp < #{startOfWeek} + CAST(7*24*3600 AS LONG) * 1000 AND creatorId = #{creatorId}")
    @ConstructorArgs({
            @Arg(column = "creatorId", javaType = UUID.class),
            @Arg(column = "eventId", javaType = UUID.class),
            @Arg(column = "type", javaType = EventType.class),
            @Arg(column = "topic", javaType = String.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "timestamp", javaType = long.class),
            @Arg(column = "createdTimestamp", javaType = long.class)
    })
    List<Event> getEventsForWeek(long startOfWeek, UUID creatorId);

    @Select("SELECT * FROM Events WHERE creatorId = #{creatorId}")
    @ConstructorArgs({
            @Arg(column = "creatorId", javaType = UUID.class),
            @Arg(column = "eventId", javaType = UUID.class),
            @Arg(column = "type", javaType = EventType.class),
            @Arg(column = "topic", javaType = String.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "timestamp", javaType = long.class),
            @Arg(column = "createdTimestamp", javaType = long.class)
    })
    List<Event> getEvents(UUID userId);

    @Select("SELECT * FROM Events WHERE eventId = #{eventId}")
    @ConstructorArgs({
            @Arg(column = "eventId", javaType = UUID.class),
    })
    List<UUID> getUUIDs(UUID eventId);

    @Insert("INSERT INTO Events(creatorId, eventId, type, topic, hour, timestamp, createdTimestamp) VALUES(#{creatorId}, #{eventId}, #{type}, #{topic}, #{hour}, #{timestamp}, #{createdTimestamp})")
    void persistEvent(Event event);

    @Delete("DELETE FROM Events WHERE creatorId = #{creatorId} AND eventId = #{eventId}")
    void deleteEvent(UUID creatorId, UUID eventId);
}
