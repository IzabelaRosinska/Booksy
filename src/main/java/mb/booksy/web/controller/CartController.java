package mb.booksy.web.controller;

import mb.booksy.services.CartService;
import mb.booksy.services.ItemService;
import mb.booksy.web.model.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class CartController {

    private final ItemService itemService;
    private final CartService cartService;

    public CartController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping({"/cart/{id}", "cart/{id}.html"})
    public String getCart(@PathVariable("id") Long cartId, Model model) {
        List<ItemDto> results = itemService.findAllCartItem(cartId);
        if (!results.isEmpty())
            model.addAttribute("selections", results);
        double price = itemService.countPrice(cartId);
        double newPrice = itemService.countPrice(cartId) * 90;
        newPrice = Math.round(newPrice);
        newPrice /= 100;
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("final", "Zapłać po rabacie " + newPrice + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));
        return "cart";
    }

    @GetMapping({"/cart/delete", "cart/delete.html"})
    public String deleteFromCart(@RequestParam("cartId") String cartId, @RequestParam("itemId") String itemId, Model model) {
        cartService.deleteItemFromCart(Long.valueOf(cartId), Long.valueOf(itemId));
        return "redirect:/cart/" + cartId;
    }

    @PostMapping({"/cart/update", "cart/update.html"})
    public String updateItemInCart(@RequestParam("cartId") String cartId, @RequestParam("itemId") String itemId, @RequestParam("new_number") String new_number, Model model) {
        cartService.updateItemNumber(Long.valueOf(cartId), Long.valueOf(itemId));
        return "redirect:/cart/" + cartId;
    }


}
