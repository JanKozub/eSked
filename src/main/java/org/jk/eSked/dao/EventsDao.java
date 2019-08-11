package org.jk.eSked.dao;

import org.apache.ibatis.annotations.*;
import org.jk.eSked.model.event.EventType;
import org.jk.eSked.model.event.ScheduleEvent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventsDao {
    @Select("SELECT * FROM Events WHERE timestamp >= #{startOfWeek} AND timestamp < #{startOfWeek} + CAST(7*24*3600 AS LONG) * 1000 AND userId = #{userId}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "id", javaType = UUID.class),
            @Arg(column = "timestamp", javaType = long.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "eventType", javaType = EventType.class),
            @Arg(column = "topic", javaType = String.class),
            @Arg(column = "createdDate", javaType = long.class)
    })
    List<ScheduleEvent> getEvents(long startOfWeek, UUID userId);

    @Select("SELECT * FROM Events WHERE userId = #{userId}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "id", javaType = UUID.class),
            @Arg(column = "timestamp", javaType = long.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "eventType", javaType = EventType.class),
            @Arg(column = "topic", javaType = String.class),
            @Arg(column = "createdDate", javaType = long.class)
    })
    List<ScheduleEvent> getAllEvents(UUID userId);

    @Insert("INSERT INTO Events(userId, id, timestamp, hour, eventType, topic, createdDate) VALUES(#{userId}, #{id}, #{dateTimestamp}, #{hour}, #{eventType}, #{topic}, #{createdDateTimestamp})")
    void persistEvent(ScheduleEvent event);

    @Delete("DELETE FROM Events WHERE id = #{id}")
    void deleteEvent(ScheduleEvent event);
}
