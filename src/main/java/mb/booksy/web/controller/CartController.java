package mb.booksy.web.controller;

import mb.booksy.domain.user.Client;
import mb.booksy.services.*;
import mb.booksy.web.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class CartController {

    private final ItemService itemService;
    private final CartService cartService;

    public CartController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    // cart details

    @GetMapping({"/cart", "/cart.html"})
    public String getCart(Model model) {
        List<ItemDto> results = itemService.findAllCartItem();
        if (!results.isEmpty())
            model.addAttribute("selections", results);

        model.addAttribute("desc", "Cena produktów: " + itemService.countPrice() + " PLN");
        model.addAttribute("final", "Zapłać po rabacie " + itemService.countDiscount() + " PLN");

        return "cart";
    }

    // cart modification

    @GetMapping({"/cart/delete", "/cart/delete.html"})
    public String deleteFromCart(@RequestParam("itemId") String itemId) {
        cartService.deleteItemFromCart(Long.valueOf(itemId));
        return "redirect:/cart";
    }

    @PostMapping({"/cart/update", "/cart/update.html"})
    public String updateItemInCart(@RequestParam("itemId") String itemId, @RequestParam("new_number") String new_number) {
        cartService.updateItemNumber(Long.valueOf(itemId), Integer.valueOf(new_number));
        return "redirect:/cart";
    }
}
