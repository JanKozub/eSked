package org.jk.eSked.dao;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.jk.eSked.services.emailService.EmailData;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailDao {
    @Select("SELECT * FROM EmailInfo")
    @ConstructorArgs({
            @Arg(column = "stmpPort", javaType = String.class),
            @Arg(column = "stmpAuth", javaType = String.class),
            @Arg(column = "smtpStarttlsEnable", javaType = String.class),
            @Arg(column = "host", javaType = String.class),
            @Arg(column = "email", javaType = String.class),
            @Arg(column = "password", javaType = String.class),
    })
    EmailData getEmailData();

    @Insert("INSERT INTO EmailInfo(stmpPort, stmpAuth, smtpStarttlsEnable, host, email, password) VALUES(#{stmpPort}, #{stmpAuth}, #{smtpStarttlsEnable}, #{host}, #{email}, #{password})")
    void setEmailData(EmailData emailData);
}
