package mb.booksy.domain.user;

import lombok.*;
import mb.booksy.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class User extends BaseEntity {

    public User(Long id, String login, String name, String surname, String password, String userRole) {
        super(id);
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.userRole = userRole;
    }

    @NotBlank
    @NotEmpty
    @Column(name = "login")
    private String login;

    @NotBlank
    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotBlank
    @NotEmpty
    @Column(name = "surname")
    private String surname;

    @NotBlank
    @NotEmpty
    @Column(name = "password")
    private String password;

    @Column(name = "locked")
    private boolean locked;

    @NotNull
    @Column(name = "role")
    private String userRole;

/*
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>(0);
        list.add(userRole);
        return list;
    }

 */

/*
    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    */
}