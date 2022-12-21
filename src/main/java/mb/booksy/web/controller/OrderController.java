package mb.booksy.web.controller;

import mb.booksy.services.CartService;
import mb.booksy.services.ItemService;
import mb.booksy.services.OrderService;
import mb.booksy.services.UserAuthenticationService;
import mb.booksy.web.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class OrderController {

    private final ItemService itemService;
    private final CartService cartService;
    private final OrderService orderService;
    private final UserAuthenticationService userAuthenticationService;

    public OrderController(ItemService itemService, CartService cartService, OrderService orderService, UserAuthenticationService userAuthenticationService) {
        this.itemService = itemService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.userAuthenticationService = userAuthenticationService;
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
