package mb.booksy.web.controller;

import mb.booksy.services.CartService;
import mb.booksy.services.ItemService;
import mb.booksy.web.model.CourierDeliveryDto;
import mb.booksy.web.model.ItemDto;
import mb.booksy.web.model.PersonDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class CartController {

    private final ItemService itemService;
    private final CartService cartService;
    private String response = "";

    public CartController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    //cart modification

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
        model.addAttribute("alert", response);

        return "cart";
    }

    @GetMapping({"/cart/delete", "cart/delete.html"})
    public String deleteFromCart(@RequestParam("cartId") String cartId, @RequestParam("itemId") String itemId, Model model) {
        cartService.deleteItemFromCart(Long.valueOf(cartId), Long.valueOf(itemId));
        return "redirect:/cart/" + cartId;
    }

    @PostMapping({"/cart/update", "cart/update.html"})
    public String updateItemInCart(@RequestParam("cartId") String cartId, @RequestParam("itemId") String itemId, @RequestParam("new_number") String new_number, Model model) {
        response = cartService.updateItemNumber(Long.valueOf(cartId), Long.valueOf(itemId), Integer.valueOf(new_number));
        return "redirect:/cart/" + cartId;
    }

    //extra personal information

    @GetMapping({"/person/{cartId}", "person/{cartId}.html", "/person"})
    public String getPersonDetails(@RequestParam("cartId") Long cartId, PersonDto personDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        return "person";
    }

    @PostMapping({"person/{cartId}", "person/{cartId}.html", "/person"})
    public String setPersonDetails(@RequestParam("cartId") Long cartId, PersonDto person, Model model) {
        model.addAttribute("cart_id", Long.valueOf(cartId));
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        if(person != null)
            System.out.print(person.getName()); //dodac tworzenie zamwienia i dodanie opcjonalnych danych czlowieka

        return "delivery";
    }

    //choose delivery

    @GetMapping({"/delivery/{cartId}", "delivery/{cartId}.html", "/delivery"})
    public String getDeliveryMethods(@RequestParam("cartId") Long cartId, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        List<String> results = itemService.findDeliveryImage();
        model.addAttribute("d1", results.get(0));
        model.addAttribute("d2", results.get(1));
        model.addAttribute("d3", results.get(2));
        model.addAttribute("d4", results.get(3));

        return "delivery";
    }

    //delivery methods

    @GetMapping({"/inpost/{cartId}", "inpost/{cartId}.html", "/inpost"})
    public String chooseInpost(@RequestParam("cartId") Long cartId, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        return "inpost";
    }

    @GetMapping({"/kurier/{cartId}", "kurier/{cartId}.html", "/kurier"})
    public String chooseCourier(@RequestParam("cartId") Long cartId, CourierDeliveryDto courierDeliveryDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        return "courier";
    }

    @PostMapping({"/kurier/{cartId}", "kurier/{cartId}.html", "/kurier"})
    public String saveCourierDelivery(@RequestParam("cartId") Long cartId, CourierDeliveryDto courierDeliveryDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));
        if(courierDeliveryDto != null)
            System.out.print(courierDeliveryDto.getAddress1()); //zapisanie danych dostawy
        return "payment";
    }

    @GetMapping({"/zabka/{cartId}", "zabka/{cartId}.html", "/zabka"})
    public String chooseZabka(@RequestParam("cartId") Long cartId, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        return "zabka";
    }

    @GetMapping({"/ruch/{cartId}", "ruch/{cartId}.html", "/ruch"})
    public String chooseRuch(@RequestParam("cartId") Long cartId, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        return "ruch";
    }

    @GetMapping({"/payment/{cartId}", "payment/{cartId}.html", "/payment"})
    public String getPaymentMethods(@RequestParam("cartId") Long cartId, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        return "payment";
    }


}
