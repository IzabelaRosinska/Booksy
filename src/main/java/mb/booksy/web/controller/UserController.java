package mb.booksy.web.controller;

import mb.booksy.exceptions.UserAlreadyExistException;
import mb.booksy.services.ItemService;
import mb.booksy.services.OrderService;
import mb.booksy.services.UserAuthenticationService;
import mb.booksy.web.model.PersonDto;
import mb.booksy.web.model.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

@Controller
public class UserController {

    private final ItemService itemService;
    private final OrderService orderService;
    private final UserAuthenticationService userAuthenticationService;

    public UserController(ItemService itemService, OrderService orderService, UserAuthenticationService userAuthenticationService) {
        this.itemService = itemService;
        this.orderService = orderService;
        this.userAuthenticationService = userAuthenticationService;
    }

    // user authentication

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
            userAuthenticationService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException e) {
            model.addAttribute("message", "Takie konto już istnieje");
            return "signup";
        }
        return "login";
    }

    // extra personal information for delivery

    @GetMapping({"/receiver", "/receiver.html"})
    public String getPersonDetails(PersonDto personDto, Model model) {
        double price = itemService.countDiscount(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        return "person";
    }

    @PostMapping({"/receiver", "/receiver.html"})
    public String setPersonDetails(PersonDto person, Model model) {
        double price = itemService.countDiscount(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        orderService.saveOrder(person);

        return "redirect:/delivery";
    }
}
