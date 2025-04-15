package io.github.artemnefedov.rstech.config.security;

public enum Authority {
    READ("read"),
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete");

    private final String authority;

    Authority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
