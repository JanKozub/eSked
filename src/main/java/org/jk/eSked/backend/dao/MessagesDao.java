package org.jk.eSked.backend.dao;

import org.apache.ibatis.annotations.*;
import org.jk.eSked.backend.model.Message;

import java.util.Collection;
import java.util.UUID;

public interface MessagesDao {

    @Select("SELECT * FROM Messages WHERE userId = #{userId}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "messageId", javaType = UUID.class),
            @Arg(column = "timestamp", javaType = long.class),
            @Arg(column = "text", javaType = String.class),
            @Arg(column = "checkedFlag", javaType = boolean.class)
    })
    Collection<Message> getMessagesForUser(UUID userId);

    @Select("SELECT * FROM Messages WHERE userId = #{userId} AND checkedFlag = #{checkedFlag}")
    @ConstructorArgs({
            @Arg(column = "userId", javaType = UUID.class),
            @Arg(column = "messageId", javaType = UUID.class),
            @Arg(column = "timestamp", javaType = long.class),
            @Arg(column = "text", javaType = String.class),
            @Arg(column = "checkedFlag", javaType = boolean.class)
    })
    Collection<Message> getUncheckedMessagesForUser(UUID userId, boolean checkedFlag);

    @Insert("INSERT INTO Messages(userId, messageId, timestamp, text, checkedFlag) VALUES(#{userId}, #{messageId}, #{timestamp}, #{text}, #{checkedFlag})")
    void addMessageForUser(Message message);

    @Update("UPDATE Messages SET checkedFlag = #{checkedFlag} WHERE messageId = #{messageId}")
    void setCheckedFlag(UUID messageId, boolean checkedFlag);

    @Select("SELECT * FROM Messages WHERE messageId = #{messageId}")
    @ConstructorArgs({
            @Arg(column = "messageId", javaType = UUID.class),
    })
    Collection<Message> doesMessageIdExists(UUID messageId);
}
