package org.jk.eSked.services.emailService;

public class EmailData {
    private String stmpPort;
    private String stmpAuth;
    private String smtpStarttlsEnable;
    private String host;
    private String email;
    private String password;

    public EmailData(String stmpPort, String stmoAuth, String smtpStarttlsEnable, String host, String email, String password) {
        this.stmpPort = stmpPort;
        this.stmpAuth = stmoAuth;
        this.smtpStarttlsEnable = smtpStarttlsEnable;
        this.host = host;
        this.email = email;
        this.password = password;
    }

    public String getStmpPort() {
        return stmpPort;
    }

    public void setStmpPort(String stmpPort) {
        this.stmpPort = stmpPort;
    }

    public String getStmpAuth() {
        return stmpAuth;
    }

    public void setStmpAuth(String stmpAuth) {
        this.stmpAuth = stmpAuth;
    }

    public String getSmtpStarttlsEnable() {
        return smtpStarttlsEnable;
    }

    public void setSmtpStarttlsEnable(String smtpStarttlsEnable) {
        this.smtpStarttlsEnable = smtpStarttlsEnable;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
