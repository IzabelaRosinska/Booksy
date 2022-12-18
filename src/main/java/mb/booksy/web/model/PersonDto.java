package mb.booksy.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDto {

    private Long id;
    private String name;
    private String login;
    private String password;
    private String surname;
    private String email;
    private String phone;
}
