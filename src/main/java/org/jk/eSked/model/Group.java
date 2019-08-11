package org.jk.eSked.model;

public class Group {
    private final boolean isAccepted;
    private final String name;
    private final int code;

    public Group(boolean isAccepted, String name, int code) {
        this.isAccepted = isAccepted;
        this.name = name;
        this.code = code;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

}
