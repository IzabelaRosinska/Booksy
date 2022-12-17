package mb.booksy.web.controller;

import mb.booksy.services.ItemService;
import mb.booksy.web.model.ItemDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CartController {

    private final ItemService itemService;

    public CartController(ItemService itemService) {
        this.itemService = itemService;
    }

    /*
    @GetMapping("/cart")
    public String getCartPage(){
        return "cart";
    }

 */

    @GetMapping({"/cart/{id}", "cart/{id}.html"})
    public String getCart(@PathVariable("id") Long cartId, Model model) {
        List<ItemDto> results = itemService.findAllCartItem(cartId);
        if (!results.isEmpty())
            model.addAttribute("selections", results);

        return "cart";
    }
}
