package org.jk.eSked.dao;

import org.apache.ibatis.annotations.*;
import org.jk.eSked.model.entry.GroupEntry;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupsDao {

    @Insert("INSERT INTO Groups(isAccepted, name, groupCode, leaderId, hour, day, subject, createdDate) VALUES(#{isAccepted}, #{name}, #{groupCode}, #{leaderId}, #{hour}, #{day}, #{subject}, #{createdDate})")
    void addEntryToGroup(boolean isAccepted, String name, int groupCode, UUID leaderId, int hour, int day, String subject, long createdDate);

    @Select("SELECT * FROM Groups")
    @ConstructorArgs({
            @Arg(column = "isAccepted", javaType = boolean.class),
            @Arg(column = "name", javaType = String.class),
            @Arg(column = "groupCode", javaType = int.class),
            @Arg(column = "leaderId", javaType = UUID.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "day", javaType = int.class),
            @Arg(column = "subject", javaType = String.class),
            @Arg(column = "createdDate", javaType = long.class)
    })
    List<GroupEntry> getGroups();

    @Select("SELECT * FROM Groups WHERE groupCode = #{groupCode}")
    @ConstructorArgs({
            @Arg(column = "isAccepted", javaType = boolean.class),
            @Arg(column = "name", javaType = String.class),
            @Arg(column = "groupCode", javaType = int.class),
            @Arg(column = "leaderId", javaType = UUID.class),
            @Arg(column = "hour", javaType = int.class),
            @Arg(column = "day", javaType = int.class),
            @Arg(column = "subject", javaType = String.class),
            @Arg(column = "createdDate", javaType = long.class)
    })
    List<GroupEntry> getGroupEntries(int groupCode);

    @Delete("DELETE FROM Groups WHERE groupCode = #{groupCode} AND hour = #{hour} AND day = #{day}")
    void deleteGroupEntry(int groupCode, int hour, int day);

    @Delete("DELETE FROM Groups WHERE groupCode = #{groupCode}")
    void deleteGroup(int groupCode);

    @Update("UPDATE Groups SET isAccepted = #{isAccepted} WHERE groupCode = #{groupCode}")
    void setGroupAccepted(int groupCode, boolean isAccepted);
}