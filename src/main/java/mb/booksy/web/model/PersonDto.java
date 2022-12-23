package mb.booksy.web.model;

import lombok.Getter;
import lombok.Setter;
import mb.booksy.validators.PasswordMatches;
import mb.booksy.validators.ValidEmail;
import mb.booksy.validators.ValidName;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PersonDto {

    private Long id;

    private String name;

    private String login;

    private String password;
    private String matchingPassword;

    private String surname;

    private String email;

    private String phone;
}
