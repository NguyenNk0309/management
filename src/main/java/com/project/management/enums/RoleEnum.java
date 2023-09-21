package com.project.management.enums;

public enum RoleEnum {
    ADMIN("ADMIN"),
    USER("USER");

    public final String desc;

    RoleEnum(String desc) {
        this.desc = desc;
    }
}
