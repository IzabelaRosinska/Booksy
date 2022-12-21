package mb.booksy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String getHomePage(){
        return "home";
    }

    @GetMapping("/shop")
    public String getShopPage(){
        return "shop";
    }

}
