package ru.pasvitas.eshop.model;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
@Getter
public enum UserRole implements GrantedAuthority {
    USER("user"),
    ADMIN("admin");

    private final String dbName;

    @Override
    public String getAuthority() {
        return dbName;
    }

    public static UserRole getUserRoleByDbName(String dbName) {
        return Arrays.stream(UserRole.values())
                .filter(userRole -> userRole.getDbName().equals(dbName))
                .findAny()
                .orElse(null);
    }
}
