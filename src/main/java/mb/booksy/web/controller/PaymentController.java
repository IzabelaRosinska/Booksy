package mb.booksy.web.controller;

import mb.booksy.services.ItemService;
import mb.booksy.services.OrderService;
import mb.booksy.services.PaymentService;
import mb.booksy.services.UserAuthenticationService;
import mb.booksy.web.model.PaymentDto;
import mb.booksy.web.model.PersonDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class PaymentController {

    private final ItemService itemService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserAuthenticationService userAuthenticationService;

    public PaymentController(ItemService itemService, OrderService orderService, PaymentService paymentService, UserAuthenticationService userAuthenticationService) {
        this.itemService = itemService;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.userAuthenticationService = userAuthenticationService;
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

    // payu login

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

    // payu payment

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

    // blik payment

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
}
