package by.tms.Diplom.Entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN, MANAGER, USER;


    @Override
    public String getAuthority() {
        return name();
    }
}
