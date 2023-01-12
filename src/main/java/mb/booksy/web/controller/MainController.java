package mb.booksy.web.controller;

import mb.booksy.domain.inventory.Item;
import mb.booksy.repository.FavoriteRepository;
import mb.booksy.services.CartService;
import mb.booksy.services.ItemService;
import mb.booksy.web.model.FilterDto;
import mb.booksy.web.model.ItemDto;
import mb.booksy.web.model.ProductDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private final ItemService itemService;
    private final CartService cartService;

    private final FavoriteRepository favoriteRepository;

    public MainController(ItemService itemService, CartService cartService, FavoriteRepository favoriteRepository) {
        this.itemService = itemService;
        this.cartService = cartService;
        this.favoriteRepository = favoriteRepository;
    }

    @GetMapping("/")
    public String getHomePage(){
        return "home";
    }

    @GetMapping("/shop")
    public String getShopPage(FilterDto filterDto, Model model, RedirectAttributes redirectAttributes){

        List<ItemDto> items;
        items = itemService.findAllItem();

        if (filterDto != null) {
            if (filterDto.getProducerName() != null && !filterDto.getProducerName().isEmpty()) {
                items = items
                        .stream()
                        .filter((itemDto -> itemDto.getProducerName().toLowerCase()
                        .contains(filterDto.getProducerName().toLowerCase())))
                        .collect(Collectors.toList());
            }

            if (filterDto.getGenre() != null && !filterDto.getGenre().isEmpty()) {
                items = items
                        .stream()
                        .filter((itemDto -> itemDto.getGenre().toLowerCase()
                        .contains(filterDto.getGenre().toLowerCase())))
                        .collect(Collectors.toList());
            }

            if (filterDto.getBookType() != null && !filterDto.getBookType().isEmpty()) {
                items = items
                        .stream()
                        .filter((itemDto -> itemDto.getBookType().toLowerCase()
                        .contains(filterDto.getBookType().toLowerCase())))
                        .collect(Collectors.toList());
            }
        }

        if (!items.isEmpty()) {
            model.addAttribute("items", items);
            model.addAttribute("notFound", "");
        }
        else {
            model.addAttribute("notFound", "Nie znaleziono żadnych produktów");
        }

        redirectAttributes.addAttribute("modal", "0");

        return "shop";
    }

    @GetMapping("/shop/product")
    public String productSite(@RequestParam("id") long id, @ModelAttribute("modal") String modal,
                              ProductDto productDto, Model model, RedirectAttributes redirectAttributes) {
        ItemDto product = itemService.findByItemId(id);
        String isInCart = "";
        Integer isFavorite = 0;

        if (itemService.isInCart(id)) {
            isInCart = "Produkt znajduje się już w koszyku";
        }

        if (itemService.isFavorite(id)) {
            isFavorite = 1;
        }

        model.addAttribute("isInCart", isInCart);
        model.addAttribute("product", product);
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("modal", modal);

        return "product";
    }

    @PostMapping("/shop/product/add")
    public String addToCart(@RequestParam("id") long id, @RequestParam("numberItems") String numberItems, Model model,
                      RedirectAttributes redirectAttributes) {

        numberItems = numberItems.replaceAll(",", "");

        if (cartService.addItemToCart(id, Integer.parseInt(numberItems)) == "") {
            redirectAttributes.addAttribute("modal", "1");
        }
        else {
            redirectAttributes.addAttribute("modal", "2");
        }

        redirectAttributes.addAttribute("id", id);

        return "redirect:/shop/product";
    }

    @PostMapping("/shop/product/addfav")
    public String addToFavorites(@RequestParam("id") long id, Model model,
                            RedirectAttributes redirectAttributes) {

        itemService.addToFavorites(id);

        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("modal", "0");

        return "redirect:/shop/product";
    }

    @PostMapping("/shop/product/delfav")
    public String delFromFavorites(@RequestParam("id") long id, Model model,
                                 RedirectAttributes redirectAttributes) {

        itemService.deleteFromFavorites(id);

        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("modal", "0");

        return "redirect:/shop/product";
    }

    @PostMapping("/shop/product/addAlert")
    public String addAlert(@RequestParam("id") long id, @RequestParam("mail") String mail, Model model,
                                   RedirectAttributes redirectAttributes) {

        itemService.addAlert(mail, id);

        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("modal", "3");

        return "redirect:/shop/product";
    }

    @PostMapping("/shop/product/back")
    public String backToProduct(@RequestParam("id") long id, Model model,
                           RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("modal", "0");

        return "redirect:/shop/product";
    }

}
