package mb.booksy.web.controller;

import mb.booksy.exceptions.AuthException;
import mb.booksy.exceptions.UserAlreadyExistException;
import mb.booksy.services.UserAuthenticationService;
import mb.booksy.web.model.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MainController {

    private final UserAuthenticationService userAuthService;

    public MainController(UserAuthenticationService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @GetMapping("/")
    public String getHomePage(){
        try {
            Long clientId = userAuthService.getAuthenticatedClientId();
            System.out.print(clientId);
        }catch(AuthException | javax.security.auth.message.AuthException e) {
            System.out.print("blad");
        }
        return "home";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping({"/signup", "signup.html"})
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUserAccount(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors())
            return "signup";
        try {
            userAuthService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException e) {
            model.addAttribute("message", "Takie konto już istnieje");
            return "signup";
        }
        return "login";
    }
}
