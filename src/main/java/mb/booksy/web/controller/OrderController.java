package mb.booksy.web.controller;

import mb.booksy.domain.order.ItemInReturn;
import mb.booksy.domain.order.OrderReturn;
import mb.booksy.repository.ItemInReturnRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {

    private final ItemService itemService;
    private final CartService cartService;
    private final OrderService orderService;
    private final ItemInReturnRepository itemInReturnRepository;

    public OrderController(ItemService itemService, CartService cartService, OrderService orderService,
                           ItemInReturnRepository itemInReturnRepository) {
        this.itemService = itemService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.itemInReturnRepository = itemInReturnRepository;
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
        List<ItemDto> results = itemService.findAllOrderItem(orderId);
        if (!results.isEmpty())
            model.addAttribute("selections", results);
        return "order";
    }

    @GetMapping({"/complaint", "/complaint.html"})
    public String getComplaintDetails(@RequestParam("orderId") String orderId, ComplaintDto complaintDto, Model model) {
        List<ItemDto> items = itemService.findAllOrderItem(orderId);
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
            else {
                List<ItemDto> items = itemService.findAllOrderItem(orderId);
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
        }
        return "complaint";
    }

    @GetMapping({"/orderReturn", "/orderReturn.html"})
    public String getReturnDetails(@RequestParam("orderId") String orderId,
                                   @RequestParam("orderReturnId") String orderReturnId,
                                   @RequestParam("modal") String modal,
                                   OrderReturnDto orderReturnDto,
                                   Model model, ItemInReturnDto itemInReturnDto) {


        if (orderReturnId == null || orderReturnId == "") {

            orderReturnId = String.valueOf(orderService.createNewOrderReturn(orderId));
            model.addAttribute("orderReturnId", orderReturnId);
        }
        else {
            model.addAttribute("orderReturnId", orderReturnId);
        }


        List<ItemDto> results = itemService.findAllOrderReturnItem(Long.valueOf(orderReturnId));
        List<ItemDto> items = itemService.findAllOrderItem(orderId);

        results = itemService.orderReturnItemAddNumberFromCart(results, items);

        if (!results.isEmpty()) {
            model.addAttribute("selections", results);
            model.addAttribute("noSelections", "0");
        }
        else {
            model.addAttribute("noSelections", "1");
        }


        if (!items.isEmpty())
            model.addAttribute("items", items);


        model.addAttribute("modal", modal);

        model.addAttribute("orderId", orderId);

        return "orderReturn";
    }

    @PostMapping({"/orderReturn/add", "/orderReturn/add.html"})
    public String addItemToReturn(@RequestParam("orderId") String orderId,
                                  @RequestParam("orderReturnId") String orderReturnId,
                                  OrderReturnDto orderReturnDto, Model model,
                                  ItemInReturnDto itemInReturnDto,
                                  RedirectAttributes redirectAttributes) {
        if(itemInReturnDto != null) {
            orderService.validateItemInReturn(orderReturnId, itemInReturnDto);
        }

        redirectAttributes.addAttribute("modal", "0");
        redirectAttributes.addAttribute("orderId", orderId);
        redirectAttributes.addAttribute("orderReturnId", orderReturnId);

        return "redirect:/orderReturn";
    }

    @PostMapping({"/orderReturn/update", "/orderReturn/update.html"})
    public String updateItemToReturn(@RequestParam("orderId") String orderId,
                                  @RequestParam("orderReturnId") String orderReturnId,
                                  OrderReturnDto orderReturnDto, Model model,
                                     @RequestParam("new_number") String new_number,
                                     @RequestParam("number") String number,
                                     @RequestParam("itemId") String itemId,
                                     ItemInReturnDto itemInReturnDto,
                                  RedirectAttributes redirectAttributes) {

        try {
            itemService.updateItemNumber(Integer.valueOf(new_number), Integer.valueOf(number), Long.valueOf(orderReturnId), Long.valueOf(itemId));
        } catch (Exception exception) {

        }

        redirectAttributes.addAttribute("modal", "0");
        redirectAttributes.addAttribute("orderId", orderId);
        redirectAttributes.addAttribute("orderReturnId", orderReturnId);

        return "redirect:/orderReturn";
    }

    @GetMapping({"/orderReturn/delete", "/orderReturn/delete.html"})
    public String deleteItemToReturn(@RequestParam("orderId") String orderId,
                                     @RequestParam("orderReturnId") String orderReturnId,
                                     @RequestParam("itemId") String itemId,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {

        itemService.deleteItemInOrderReturn(Long.valueOf(orderReturnId), Long.valueOf(itemId));

        redirectAttributes.addAttribute("modal", "0");
        redirectAttributes.addAttribute("orderId", orderId);
        redirectAttributes.addAttribute("orderReturnId", orderReturnId);

        return "redirect:/orderReturn";
    }

    @GetMapping({"/orderReturn/validate", "/orderReturn/validate.html"})
    public String validateOrderReturn(@RequestParam("orderId") String orderId,
                                     @RequestParam("orderReturnId") String orderReturnId,
                                     OrderReturnDto orderReturnDto, Model model,
                                     ItemInReturnDto itemInReturnDto,
                                     RedirectAttributes redirectAttributes) {

        if (orderService.validOrderReturn(orderReturnId)) {
            return "redirect:/account";
        }
        else {
            redirectAttributes.addAttribute("modal", "1");
            redirectAttributes.addAttribute("orderId", orderId);
            redirectAttributes.addAttribute("orderReturnId", orderReturnId);

            return "redirect:/orderReturn";
        }
    }

    @GetMapping({"/orderReturn/delete/orderReturn", "/orderReturn/delete/orderReturn.html"})
    public String deleteOrderReturn(@RequestParam("orderId") String orderId,
                                      @RequestParam("orderReturnId") String orderReturnId,
                                      OrderReturnDto orderReturnDto, Model model,
                                      ItemInReturnDto itemInReturnDto,
                                      RedirectAttributes redirectAttributes) {

        orderService.deleteOrderReturn(orderReturnId);
        redirectAttributes.addAttribute("orderId", orderId);
        redirectAttributes.addAttribute("modal", "0");

        return "redirect:/order";
    }

    @GetMapping({"/account", "/account.html"})
    public String getAccountDetails(ComplaintDto complaintDto, Model model) {
        List<ComplaintDto> complaints = orderService.findAllUserComplaints();
        if (!complaints.isEmpty())
            model.addAttribute("selections", complaints);

        List<OrderReturnDto> orderReturns = orderService.findAllUserOrderReturns();
        if (!orderReturns.isEmpty())
            model.addAttribute("selectionsReturns", orderReturns);

        return "account";
    }

    @GetMapping({"/account/orderReturn", "/account/orderReturn.html"})
    public String getAccountOrderReturnDetails(@RequestParam("orderReturnId") String orderReturnId,
                                               OrderReturnDto orderReturnDto,
                                               Model model) {

        List<ItemDto> results = itemService.findAllOrderReturnItem(Long.valueOf(orderReturnId));


        if (!results.isEmpty()) {
            model.addAttribute("selections", results);
        }

        model.addAttribute("orderReturnDto", orderService.findOrderReturnById(orderReturnId));

        return "orderReturnDetails";
    }
}
