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
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final UserAuthenticationService userAuthenticationService;

    public CartController(ItemService itemService, CartService cartService, DeliveryService deliveryService, PaymentService paymentService, OrderService orderService, UserAuthenticationService userAuthenticationService) {
        this.itemService = itemService;
        this.cartService = cartService;
        this.deliveryService = deliveryService;
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.userAuthenticationService = userAuthenticationService;
    }

    // cart modification

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

    // extra personal information

    @GetMapping({"/receiver", "/receiver.html"})
    public String getPersonDetails(PersonDto personDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        return "person";
    }

    @PostMapping({"/receiver", "/receiver.html"})
    public String setPersonDetails(PersonDto person, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        orderService.saveOrder(person);

        return "redirect:/delivery";
    }

    //choose delivery

    @GetMapping({"/delivery", "/delivery.html"})
    public String getDeliveryMethods(Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        List<String> results = itemService.findDeliveryImage();
        model.addAttribute("d1", results.get(0));
        model.addAttribute("d2", results.get(1));
        model.addAttribute("d3", results.get(2));
        model.addAttribute("d4", results.get(3));

        return "delivery";
    }

    // inpost delivery

    @GetMapping({"/inpost", "/inpost.html"})
    public String chooseInpost(InpostBoxDto inpostBoxDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        List<InpostBoxDto> boxes =  deliveryService.getAllInpostBoxes();
        model.addAttribute("boxes", boxes);

        return "inpost";
    }

    @PostMapping({"/inpost", "/inpost.html"})
    public String saveInpostDelivery(InpostBoxDto inpostBoxDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
        if(inpostBoxDto != null) {
            deliveryService.saveInpostDelivery(inpostBoxDto);
            return "redirect:/payment";
        }
        return "inpost";
    }

    // courier delivery

    @GetMapping({"/kurier", "/kurier.html"})
    public String chooseCourier(CourierDeliveryDto courierDeliveryDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        return "courier";
    }

    @PostMapping({"/kurier", "/kurier.html"})
    public String saveCourierDelivery(CourierDeliveryDto courierDeliveryDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        if(courierDeliveryDto != null) {
            deliveryService.saveCourierDelivery(courierDeliveryDto);
            return "redirect:/payment";
        }
        return "courier";
    }

    // zabka delivery

    @GetMapping({"/zabka", "/zabka.html"})
    public String chooseZabka(DeliveryPointDto deliveryPointDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        List<DeliveryPointDto> points =  deliveryService.getAllZabkaPoints();
        model.addAttribute("points", points);

        return "zabka";
    }

    @PostMapping({"/zabka", "/zabka.html"})
    public String saveZabkaDelivery(DeliveryPointDto deliveryPointDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        if(deliveryPointDto != null) {
            deliveryService.savePointDelivery(deliveryPointDto);
            return "redirect:/payment";
        }
        return "zabka";
    }

    // ruch delivery

    @GetMapping({"/ruch", "/ruch.html"})
    public String chooseRuch(DeliveryPointDto deliveryPointDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        List<DeliveryPointDto> points = deliveryService.getAllRuchPoints();
        model.addAttribute("points", points);

        return "ruch";
    }

    @PostMapping({"/ruch", "/ruch"})
    public String saveRuchDelivery(DeliveryPointDto deliveryPointDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        if(deliveryPointDto != null) {
            deliveryService.savePointDelivery(deliveryPointDto);
            return "redirect:/payment";
        }
        return "ruch";
    }

    // payment methods

    @GetMapping({"/payment", "/payment.html"})
    public String getPaymentMethods(Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        List<String> results = paymentService.findPaymentImage();
        model.addAttribute("d1", results.get(0));
        model.addAttribute("d2", results.get(1));

        return "payment";
    }

    // payu

    @GetMapping({"/payu", "/payu.html"})
    public String getBanks(Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

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

    // payu login details

    @GetMapping({"/bank", "/bank.html"})
    public String getBankLogin(PersonDto personDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        return "bank";
    }

    @PostMapping({"/bank", "/bank.html"})
    public String postBankLogin(PersonDto personDto, Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");

        if(personDto != null){
            if(orderService.validateLogin(personDto))
                return "redirect:/data";
            else
                return "bank";
        }
        return "bank";
    }

    // payu details

    @GetMapping({"/data", "/data.html"})
    public String getPaymentDetails(PaymentDto paymentDto, Model model) {
        paymentDto = paymentService.createPayUPayment(paymentDto);
        model.addAttribute("paymentDto", paymentDto);
        return "data";
    }

    @PostMapping({"/data", "/data.html"})
    public String postData( PaymentDto paymentDto, Model model) {
        if(paymentDto != null) {
            paymentService.updateOrder(paymentDto);
            return "redirect:/orders";
        }
        return "data";
    }

    @GetMapping({"/blik", "/blik.html"})
    public String getBlikDetails(PaymentDto paymentDto, Model model) {
        paymentDto = paymentService.createBlikPayment();
        model.addAttribute("paymentDto", paymentDto);
        return "blik";
    }

    @PostMapping({"/blik", "/blik.html"})
    public String postBlik(PaymentDto paymentDto, Model model) {
        if(paymentDto != null) {
            paymentService.updateOrder(paymentDto);
            return "redirect:/orders";
        }
        return "blik";
    }

    @GetMapping({"/orders", "/orders.html"})
    public String getOrders(Model model) {
        List<OrderDto> orders = orderService.findAllUserOrders();
        if (!orders.isEmpty())
            model.addAttribute("selections", orders);
        return "orders";
    }

    @GetMapping({"/order", "/order.html"})
    public String getOrder(@RequestParam("orderId") String orderId, Model model) {
        OrderDto orderDto = orderService.findOrderById(orderId);
        model.addAttribute("orderDto", orderDto);
        List<ItemDto> results = itemService.findAllOrderItem(orderId, userAuthenticationService.getAuthenticatedClientId());
        if (!results.isEmpty())
            model.addAttribute("selections", results);
        return "order";
    }

    @GetMapping({"/complaint", "/complaint.html"})
    public String getComplaintDetails(@RequestParam("orderId") String orderId, ComplaintDto complaintDto, Model model) {
        List<ItemDto> items = itemService.findAllOrderItem(orderId, userAuthenticationService.getAuthenticatedClientId());
        if (!items.isEmpty())
            model.addAttribute("items", items);

        List<ReasonDto> reasons = cartService.getComplaintReasons();
        if (!reasons.isEmpty())
            model.addAttribute("reasons", reasons);

        List<MethodDto> methods = cartService.getComplaintMethods();
        if (!methods.isEmpty())
            model.addAttribute("methods", methods);
        model.addAttribute("orderId", orderId);

        return "complaint";
    }

    @PostMapping({"/complaint", "/complaint.html"})
    public String setComplaintDetails(@RequestParam("orderId") String orderId, ComplaintDto complaintDto, Model model) {
        if(complaintDto != null){
            if(orderService.validateComplaint(orderId, complaintDto))
                return "redirect:/account";
            else
                return "complaint";
        }
        return "complaint";
    }

    @GetMapping({"/account", "/accountt.html"})
    public String getAccountDetails(ComplaintDto complaintDto, Model model) {
        List<ComplaintDto> complaints = orderService.findAllUserComplaints(userAuthenticationService.getAuthenticatedClientId());
        if (!complaints.isEmpty())
            model.addAttribute("selections", complaints);

        return "account";
    }
}
