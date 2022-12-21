package mb.booksy.web.controller;

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
    private final UserAuthenticationService userAuthenticationService;

    public CartController(ItemService itemService, CartService cartService, UserAuthenticationService userAuthenticationService) {
        this.itemService = itemService;
        this.cartService = cartService;
        this.userAuthenticationService = userAuthenticationService;
    }

    // cart details

    @GetMapping({"/cart", "/cart.html"})
    public String getCart(Model model) {
        Long cartId = userAuthenticationService.getAuthenticatedClientId();
        List<ItemDto> results = itemService.findAllCartItem(cartId);
        if (!results.isEmpty())
            model.addAttribute("selections", results);

        model.addAttribute("desc", "Cena produktów: " + itemService.countPrice(cartId) + " PLN");
        model.addAttribute("final", "Zapłać po rabacie " + itemService.countDiscount(cartId) + " PLN");

        return "cart";
    }

    // cart modification

    @GetMapping({"/cart/delete", "/cart/delete.html"})
    public String deleteFromCart(@RequestParam("itemId") String itemId) {
        cartService.deleteItemFromCart(userAuthenticationService.getAuthenticatedClientId(), Long.valueOf(itemId));
        return "redirect:/cart";
    }

    @PostMapping({"/cart/update", "/cart/update.html"})
    public String updateItemInCart(@RequestParam("itemId") String itemId, @RequestParam("new_number") String new_number) {
        cartService.updateItemNumber(userAuthenticationService.getAuthenticatedClientId(), Long.valueOf(itemId), Integer.valueOf(new_number));

        return "redirect:/cart";
    }
}
