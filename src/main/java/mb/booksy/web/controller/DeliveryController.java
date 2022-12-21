package mb.booksy.web.controller;

import mb.booksy.services.DeliveryService;
import mb.booksy.services.ItemService;
import mb.booksy.services.UserAuthenticationService;
import mb.booksy.web.model.CourierDeliveryDto;
import mb.booksy.web.model.DeliveryPointDto;
import mb.booksy.web.model.InpostBoxDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class DeliveryController {

    private final ItemService itemService;
    private final DeliveryService deliveryService;
    private final UserAuthenticationService userAuthenticationService;

    public DeliveryController(ItemService itemService, DeliveryService deliveryService, UserAuthenticationService userAuthenticationService) {
        this.itemService = itemService;
        this.deliveryService = deliveryService;
        this.userAuthenticationService = userAuthenticationService;
    }

    private void setCartSummaryParameters(Model model) {
        double price = itemService.countPrice(userAuthenticationService.getAuthenticatedClientId());
        model.addAttribute("desc", "Cena produktów: " + price + " PLN");
    }

    // choose delivery

    @GetMapping({"/delivery", "/delivery.html"})
    public String getDeliveryMethods(Model model) {
        setCartSummaryParameters(model);

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
        setCartSummaryParameters(model);

        List<InpostBoxDto> boxes =  deliveryService.getAllInpostBoxes();
        model.addAttribute("boxes", boxes);

        return "inpost";
    }

    @PostMapping({"/inpost", "/inpost.html"})
    public String saveInpostDelivery(InpostBoxDto inpostBoxDto, Model model) {
        setCartSummaryParameters(model);
        if(inpostBoxDto != null) {
            deliveryService.saveInpostDelivery(inpostBoxDto);
            return "redirect:/payment";
        }
        return "inpost";
    }

    // courier delivery

    @GetMapping({"/kurier", "/kurier.html"})
    public String chooseCourier(CourierDeliveryDto courierDeliveryDto, Model model) {
        setCartSummaryParameters(model);

        return "courier";
    }

    @PostMapping({"/kurier", "/kurier.html"})
    public String saveCourierDelivery(CourierDeliveryDto courierDeliveryDto, Model model) {
        setCartSummaryParameters(model);

        if(courierDeliveryDto != null) {
            deliveryService.saveCourierDelivery(courierDeliveryDto);
            return "redirect:/payment";
        }
        return "courier";
    }

    // zabka delivery

    @GetMapping({"/zabka", "/zabka.html"})
    public String chooseZabka(DeliveryPointDto deliveryPointDto, Model model) {
        setCartSummaryParameters(model);

        List<DeliveryPointDto> points =  deliveryService.getAllZabkaPoints();
        model.addAttribute("points", points);

        return "zabka";
    }

    @PostMapping({"/zabka", "/zabka.html"})
    public String saveZabkaDelivery(DeliveryPointDto deliveryPointDto, Model model) {
        setCartSummaryParameters(model);

        if(deliveryPointDto != null) {
            deliveryService.savePointDelivery(deliveryPointDto);
            return "redirect:/payment";
        }
        return "zabka";
    }

    // ruch delivery

    @GetMapping({"/ruch", "/ruch.html"})
    public String chooseRuch(DeliveryPointDto deliveryPointDto, Model model) {
        setCartSummaryParameters(model);

        List<DeliveryPointDto> points = deliveryService.getAllRuchPoints();
        model.addAttribute("points", points);

        return "ruch";
    }

    @PostMapping({"/ruch", "/ruch"})
    public String saveRuchDelivery(DeliveryPointDto deliveryPointDto, Model model) {
        setCartSummaryParameters(model);

        if(deliveryPointDto != null) {
            deliveryService.savePointDelivery(deliveryPointDto);
            return "redirect:/payment";
        }
        return "ruch";
    }
}
