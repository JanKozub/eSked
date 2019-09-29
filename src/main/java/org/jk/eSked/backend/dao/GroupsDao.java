package org.jk.eSked.backend.dao;

import org.apache.ibatis.annotations.*;
import org.jk.eSked.backend.model.Group;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupsDao {

    @Insert("INSERT INTO Groups(name, groupCode, leaderId, isAccepted, createdDate) VALUES(#{name}, #{groupCode}, #{leaderId}, #{isAccepted}, #{createdDate})")
    void addGroup(Group group);

    @Delete("DELETE FROM Groups WHERE groupCode = #{groupCode}")
    void deleteGroup(int groupCode);

    @Select("SELECT * FROM Groups")
    @ConstructorArgs({
            @Arg(column = "name", javaType = String.class),
            @Arg(column = "groupCode", javaType = int.class),
            @Arg(column = "leaderId", javaType = UUID.class),
            @Arg(column = "isAccepted", javaType = boolean.class),
            @Arg(column = "createdDate", javaType = long.class),
    })
    List<Group> getGroups();

    @Select("SELECT * FROM Groups")
    @ConstructorArgs({
            @Arg(column = "name", javaType = String.class),
    })
    List<String> getGroupsNames();

    @Select("SELECT * FROM Groups WHERE groupCode = #{groupCode}")
    @ConstructorArgs({
            @Arg(column = "name", javaType = String.class),
    })
    String getGroupName(int groupCode);

    @Select("SELECT * FROM Groups WHERE name = #{name}")
    @ConstructorArgs({
            @Arg(column = "leaderId", javaType = UUID.class),
    })
    UUID getLeaderIdName(String name);

    @Select("SELECT * FROM Groups WHERE groupCode = #{groupCode}")
    @ConstructorArgs({
            @Arg(column = "leaderId", javaType = UUID.class),
    })
    UUID getLeaderIdCode(int groupCode);


    @Update("UPDATE Groups SET isAccepted = #{isAccepted} WHERE groupCode = #{groupCode}")
    void setGroupAccepted(int groupCode, boolean isAccepted);
}