package org.jk.eSked.backend.dao;

import org.apache.ibatis.annotations.*;
import org.jk.eSked.backend.model.schedule.ScheduleHour;

import java.util.Collection;
import java.util.UUID;

public interface HoursDao {

    @Select("SELECT * FROM Hours WHERE userId = #{userId} AND hour = #{hour}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "data", javaType = String.class)
    })
    ScheduleHour getHour(UUID userId, int hour);

    @Select("SELECT * FROM Hours WHERE userId = #{userId}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "data", javaType = String.class)
    })
    Collection<ScheduleHour> getHours(UUID userId);

    @Insert("INSERT INTO Hours(userId, hour, data) VALUES(#{userId}, #{hour}, #{data})")
    void addHour(ScheduleHour scheduleHour);

    @Delete("DELETE FROM Hours WHERE userId = #{userId}")
    void deleteHours(UUID userId);
}
