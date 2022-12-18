package mb.booksy.web.controller;

import mb.booksy.services.CartService;
import mb.booksy.services.DeliveryService;
import mb.booksy.services.ItemService;
import mb.booksy.services.PaymentService;
import mb.booksy.web.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class CartController {

    private final ItemService itemService;
    private final CartService cartService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private String response = "";

    public CartController(ItemService itemService, CartService cartService, DeliveryService deliveryService, PaymentService paymentService) {
        this.itemService = itemService;
        this.cartService = cartService;
        this.deliveryService = deliveryService;
        this.paymentService = paymentService;
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
    public String chooseInpost(@RequestParam("cartId") Long cartId, InpostBoxDto inpostBoxDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        List<InpostBoxDto> boxes =  deliveryService.getAllInpostBoxes();
        model.addAttribute("boxes", boxes);

        return "inpost";
    }

    @PostMapping({"inpost/{cartId}", "inpost/{cartId}.html", "/inpost"})
    public String saveInpostDelivery(@RequestParam("cartId") Long cartId, InpostBoxDto inpostBoxDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));
        if(inpostBoxDto != null)
            System.out.print(inpostBoxDto.getId()); //zapisanie danych dostawy
        return "payment";
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
    public String chooseZabka(@RequestParam("cartId") Long cartId, DeliveryPointDto deliveryPointDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        List<DeliveryPointDto> points =  deliveryService.getAllZabkaPoints();
        model.addAttribute("points", points);

        return "zabka";
    }

    @PostMapping({"zabka/{cartId}", "zabka/{cartId}.html", "/zabka"})
    public String saveZabkaDelivery(@RequestParam("cartId") Long cartId, DeliveryPointDto deliveryPointDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));
        if(deliveryPointDto != null)
            System.out.print(deliveryPointDto.getId()); //zapisanie danych dostawy
        return "payment";
    }

    @GetMapping({"/ruch/{cartId}", "ruch/{cartId}.html", "/ruch"})
    public String chooseRuch(@RequestParam("cartId") Long cartId, DeliveryPointDto deliveryPointDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        List<DeliveryPointDto> points = deliveryService.getAllRuchPoints();
        model.addAttribute("points", points);

        return "ruch";
    }

    @PostMapping({"ruch/{cartId}", "ruch/{cartId}.html", "/ruch"})
    public String saveRuchDelivery(@RequestParam("cartId") Long cartId, DeliveryPointDto deliveryPointDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));
        if(deliveryPointDto != null)
            System.out.print(deliveryPointDto.getId());//zapisanie danych dostawy
        System.out.print("cartId=" + cartId);
        return "redirect:/payment/"  + cartId;
    }

    // payment methods

    @GetMapping({"/payment/{cartId}", "payment/{cartId}.html", "/payment"})
    public String getPaymentMethods(@RequestParam("cartId") Long cartId, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        List<String> results = paymentService.findPaymentImage();
        model.addAttribute("d1", results.get(0));
        model.addAttribute("d2", results.get(1));

        return "payment";
    }

    @GetMapping({"/payu/{cartId}", "payu/{cartId}.html", "/payu"})
    public String getBanks(@RequestParam("cartId") Long cartId, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        List<String> results = paymentService.findBankImage();
        model.addAttribute("d1", results.get(0));
        model.addAttribute("d2", results.get(1));
        model.addAttribute("d3", results.get(2));
        model.addAttribute("d4", results.get(3));
        model.addAttribute("d5", results.get(4));
        model.addAttribute("d6", results.get(5));
        model.addAttribute("d7", results.get(6));
        model.addAttribute("d8", results.get(7));
        model.addAttribute("d9", results.get(8));

        return "payu";
    }

    // payments

    @GetMapping({"/bank/{cartId}", "bank/{cartId}.html", "/bank"})
    public String getBankLogin(@RequestParam("cartId") Long cartId, PersonDto personDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));

        return "bank";
    }

    @PostMapping({"/bank/{cartId}", "bank/{cartId}.html", "/bank"})
    public String postBankLogin(@RequestParam("cartId") Long cartId, PersonDto personDto, Model model) {
        double price = itemService.countPrice(Long.valueOf(cartId));
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        model.addAttribute("cart_id", Long.valueOf(cartId));
        if(personDto != null)
            System.out.print(personDto.getLogin());

        return "data";
    }

    @GetMapping({"/data/{cartId}", "data/{cartId}.html", "/data"})
    public String getPaymentDetails(@RequestParam("cartId") Long cartId, PaymentDto paymentDto, Model model) {
        model.addAttribute("cart_id", Long.valueOf(cartId));

        return "data";
    }

    @PostMapping({"/data/{cartId}", "data/{cartId}.html", "/data"})
    public String postData(@RequestParam("cartId") Long cartId, PaymentDto paymentDto, Model model) {
        model.addAttribute("cart_id", Long.valueOf(cartId));
        if(paymentDto != null)
            System.out.print("jest");

        return "orders";
    }

    @GetMapping({"/blik/{cartId}", "blik/{cartId}.html", "/blik"})
    public String getBlikDetails(@RequestParam("cartId") Long cartId, PaymentDto paymentDto, Model model) {
        model.addAttribute("cart_id", Long.valueOf(cartId));

        return "blik";
    }

    @PostMapping({"/blik/{cartId}", "blik/{cartId}.html", "/blik"})
    public String postBlik(@RequestParam("cartId") Long cartId, PaymentDto paymentDto, Model model) {
        model.addAttribute("cart_id", Long.valueOf(cartId));
        if(paymentDto != null)
            System.out.print("jest");

        return "orders";
    }



}
