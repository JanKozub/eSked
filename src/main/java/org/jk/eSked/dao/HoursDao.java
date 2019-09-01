package org.jk.eSked.dao;

import org.apache.ibatis.annotations.*;
import org.jk.eSked.model.ScheduleHour;

import java.util.List;
import java.util.UUID;

public interface HoursDao {

    @Select("SELECT * FROM Hours WHERE userId = #{userId}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "data", javaType = String.class),
    })
    List<ScheduleHour> getHours(UUID userId);

    @Select("SELECT * FROM Hours WHERE userId = #{userId} AND hour = #{hour}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "data", javaType = String.class),
    })
    ScheduleHour getHour(UUID userId, int hour);

    @Insert("INSERT INTO Hours(userId, hour, data) VALUES(#{userId}, #{hour}, #{data})")
    void addHour(ScheduleHour scheduleHour);

    @Delete("DELETE FROM Hours WHERE userId = #{userId}")
    void deleteHours(UUID userId);
}
