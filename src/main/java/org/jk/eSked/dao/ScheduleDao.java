package org.jk.eSked.dao;

import org.apache.ibatis.annotations.*;
import org.jk.eSked.model.entry.ScheduleEntry;

import java.util.List;
import java.util.UUID;

public interface ScheduleDao {

    @Select("SELECT * FROM ScheduleEntry WHERE userId = #{userId}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "day", javaType = int.class),
            @Arg(column = "subject", javaType = String.class),
            @Arg(column = "createdDate", javaType = long.class)
    })
    List<ScheduleEntry> getScheduleEntries(UUID userId);

    @Insert("INSERT INTO ScheduleEntry(userId, hour, day, subject, createdDate) VALUES(#{userId}, #{hour}, #{day}, #{subject}, #{createdTimestamp})")
    void persistScheduleEntry(ScheduleEntry scheduleEntry);

    @Delete("DELETE FROM ScheduleEntry WHERE userId = #{userId} AND hour = #{hour} AND day = #{day}")
    void deleteScheduleEntry(UUID userId, int hour, int day);

    @Delete("DELETE FROM ScheduleEntry WHERE userId = #{userId}")
    void deleteScheduleEntries(UUID userId);
}
