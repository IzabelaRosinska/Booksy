package mb.booksy.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum ApplicationUserRole {
    CLIENT("ROLE_CLIENT");

    String userRole;

    ApplicationUserRole(String userRole) {
        this.userRole = userRole;
    }

    public SimpleGrantedAuthority getUserRole() {
        return new SimpleGrantedAuthority(userRole);
    }

}
