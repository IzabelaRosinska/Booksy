package mb.booksy.domain.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum ApplicationUserRole {
    CLIENT("ROLE_CLIENT"),
    PROVIDER("ROLE_PROVIDER");

    String userRole;

    ApplicationUserRole(String userRole) {
        this.userRole = userRole;
    }

    public SimpleGrantedAuthority getUserRole() {
        return new SimpleGrantedAuthority(userRole);
    }

}
